package cn.nexura.nextbi.mq;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.pinyin.PinyinUtil;
import cn.nexura.nextbi.common.ErrorCode;
import cn.nexura.nextbi.exception.BusinessException;
import cn.nexura.nextbi.exception.ThrowUtils;
import cn.nexura.nextbi.manager.AiManager;
import cn.nexura.nextbi.mapper.ChartMapper;
import cn.nexura.nextbi.model.entity.Chart;
import cn.nexura.nextbi.utils.ExcelUtils;
import com.rabbitmq.client.Channel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static cn.nexura.nextbi.constant.CommonConstant.biModelId;

/**
 * @author PeiYP
 * @since 2024年01月16日 14:46
 */
@Component
@Slf4j
public class BiMessageConsumer {

    @Resource
    private ChartMapper chartMapper;

    @Resource
    private AiManager aiManager;

    @SneakyThrows
    @RabbitListener(queues = {BiMqConstant.BI_QUEUE_NAME}, ackMode = "MANUAL")
    public void receiveMessage(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliverTag) throws IOException {

        if (StrUtil.isBlank(message)) {
            channel.basicNack(deliverTag, false, false);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "消息为空");
        }

        long chartId = Long.parseLong(message);

        Chart chart = chartMapper.selectById(chartId);

        if (chart == null) {
            channel.basicNack(deliverTag, false, false);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "无此图表");
        }
        String chartData = chart.getChartData();

        // 用户输入
        StringBuilder userInput = new StringBuilder();
        userInput.append("Analysis goal:").append("\n");


        userInput.append(chart.getGoal()).append("\n");

        // TODO: 2024/1/16 应该去其他表查询
        userInput.append("Row data:").append("\n").append(chartData).append("\n");
        // 修改任务为执行中
        Chart updateChart = new Chart();
        updateChart.setId(chart.getId());
        updateChart.setStatus("running");
        int updateRow = chartMapper.updateById(updateChart);
        if (updateRow <= 0) {
            // 修改数据库状态为失败
            channel.basicNack(deliverTag, false, false);
            handleChartUpdateError(chart.getId(), "更新体表状态失败");
            return;
        }

        String result = aiManager.doChat(biModelId, userInput.toString());
        String[] splitRes = result.split("【【【【【");
        if (splitRes.length < 3) {
            channel.basicNack(deliverTag, false, false);
            handleChartUpdateError(chart.getId(), "AI生成错误");
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
        updateRow = chartMapper.updateById(updateChartRes);
        if (updateRow <= 0) {
            channel.basicNack(deliverTag, false, false);
            // 修改数据库状态为失败
            handleChartUpdateError(chart.getId(), "更新体表状态失败");
            return;
        }

        channel.basicAck(deliverTag, false);
    }

    private void handleChartUpdateError(long chartId, String execMessage) {
        Chart updateChartRes = new Chart();
        updateChartRes.setId(chartId);
        updateChartRes.setStatus("failed");
        updateChartRes.setExecMessage(execMessage);
        int i = chartMapper.updateById(updateChartRes);
        if (i <= 0) {
            log.error("执行更新状态失败" + chartId + ":" + execMessage);
        }
    }

}
