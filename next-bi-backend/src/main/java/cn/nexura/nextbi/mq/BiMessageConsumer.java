package cn.nexura.nextbi.mq;

import cn.hutool.core.util.StrUtil;
import cn.nexura.nextbi.common.ErrorCode;
import cn.nexura.nextbi.exception.BusinessException;
import cn.nexura.nextbi.manager.AiManager;
import cn.nexura.nextbi.mapper.ChartMapper;
import cn.nexura.nextbi.model.entity.Chart;
import cn.nexura.nextbi.service.ChartService;
import cn.nexura.nextbi.sse.service.SseService;
import com.rabbitmq.client.Channel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

import static cn.nexura.nextbi.constant.CommonConstant.BI_MODEL_ID;

/**
 * @author PeiYP
 * @since 2024年01月16日 14:46
 */
@Component
@Slf4j
public class BiMessageConsumer {

    @Resource
    private ChartService chartService;

    @Resource
    private AiManager aiManager;

    @Resource
    private SseService sseService;

    @SneakyThrows
    @RabbitListener(queues = {BiMqConstant.BI_QUEUE_NAME}, ackMode = "MANUAL")
    public void receiveMessage(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliverTag) throws IOException {

        try {
            Chart chart = validMessage(message, channel, deliverTag);

            Long chartId = chart.getId();

            // 用户输入
            StringBuilder userInput = new StringBuilder();
            userInput.append("Analysis goal:").append("\n");

            userInput.append(chart.getGoal()).append("\n");

            // 2024/1/16 应该去其他表查询
            String chartData = chartService.getChartData(chart);
            userInput.append("Row data:").append("\n").append(chartData).append("\n");
            // 修改任务为执行中
            Chart updateChart = new Chart();
            updateChart.setId(chartId);
            updateChart.setStatus("running");
            boolean updateRow = chartService.updateById(updateChart);
            if (!updateRow) {
                // 修改数据库状态为失败
                channel.basicNack(deliverTag, false, false);
                handleChartUpdateError(chartId, "更新体表状态失败");
                return;
            }

            String result = null;
            try {
                result = aiManager.doChat(BI_MODEL_ID, userInput.toString());
            } catch (Exception e) {
                channel.basicNack(deliverTag, false, false);
                return;
            }
            String[] splitRes = result.split("【【【【【");
            if (splitRes.length < 3) {
                channel.basicNack(deliverTag, false, false);
                handleChartUpdateError(chartId, "AI生成错误");
                return;
            }

            String genChart = splitRes[1].trim();
            String genResult = splitRes[2].trim();

            // 修改任务为执行成功
            Chart updateChartRes = new Chart();
            updateChartRes.setId(chartId);
            updateChartRes.setStatus("succeed");
            updateChartRes.setExecMessage("生成成功");
            updateChartRes.setGenChart(genChart);
            updateChartRes.setGenResult(genResult);
            updateRow = chartService.updateById(updateChartRes);
            if (!updateRow) {
                channel.basicNack(deliverTag, false, false);
                // 修改数据库状态为失败
                handleChartUpdateError(chartId, "更新体表状态失败");
                return;
            }

            sseService.sendMessageToOneClient(chart.getUserId().toString(), "图表已经成功生成，请移步‘我的图表’查看！");
        } catch (IOException e) {
            channel.basicNack(deliverTag, false, false);
            throw new RuntimeException(e);
        }

        channel.basicAck(deliverTag, false);
    }

    @SneakyThrows
    @RabbitListener(queues = {BiMqConstant.DEAD_LETTER_QUEUE_NAME}, ackMode = "MANUAL")
    public void deadLetterMessage(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliverTag) throws IOException {

        log.info("死信队列有数据啦！----> {}", message);

        Chart chart = validMessage(message, channel, deliverTag);

        Chart updateChart = new Chart();
        updateChart.setId(chart.getId());
        updateChart.setStatus("failed");
        updateChart.setExecMessage("任务执行过程中失败，请重试");
        boolean updateRow = chartService.updateById(updateChart);
        if (!updateRow) {
            // 修改数据库状态为失败
            channel.basicNack(deliverTag, false, false);
            handleChartUpdateError(chart.getId(), "更新体表状态失败");
        }
        channel.basicAck(deliverTag, false);

        sseService.sendMessageToOneClient(chart.getUserId().toString(), "图表生成失败，请联系管理员！");


    }

    @NotNull
    private Chart validMessage(String message, Channel channel, long deliverTag) throws IOException {
        if (StrUtil.isBlank(message)) {
            channel.basicNack(deliverTag, false, false);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "消息为空");
        }

        long chartId = Long.parseLong(message);

        Chart chart = chartService.getById(chartId);

        if (chart == null) {
            channel.basicNack(deliverTag, false, false);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "无此图表");
        }
        return chart;
    }

    private void handleChartUpdateError(long chartId, String execMessage) {
        Chart updateChartRes = new Chart();
        updateChartRes.setId(chartId);
        updateChartRes.setStatus("failed");
        updateChartRes.setExecMessage(execMessage);
        boolean b = chartService.updateById(updateChartRes);
        if (!b) {
            log.error("执行更新状态失败" + chartId + ":" + execMessage);
        }
    }

}
