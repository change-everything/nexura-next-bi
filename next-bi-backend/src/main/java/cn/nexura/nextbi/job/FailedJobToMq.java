package cn.nexura.nextbi.job;

import cn.nexura.nextbi.constant.ChartGenStatus;
import cn.nexura.nextbi.model.entity.Chart;
import cn.nexura.nextbi.mq.BiMessageProducer;
import cn.nexura.nextbi.service.ChartService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author PeiYP
 * @since 2024年01月22日 14:29
 */
@Component
@Slf4j
public class FailedJobToMq {

    @Resource
    private BiMessageProducer producer;

    @Resource
    private ChartService chartService;

    // 每1个小时检查一次
    @Scheduled(fixedRate = 60 * 60 * 1000)
    public void run() {

        // 查询近 5 小时内的数据
        Date fivehoursAgoDate = new Date(new Date().getTime() - 3 * 60 * 60 * 1000L);

        List<Chart> chartList = chartService.list(Wrappers.lambdaQuery(Chart.class)
                .eq(Chart::getStatus, ChartGenStatus.FAILED)
                .ge(Chart::getUpdateTime, fivehoursAgoDate));

        if (CollectionUtils.isEmpty(chartList)) {
            log.info("nothing to produce");
            return;
        }

        List<Long> chartIds = chartList.stream()
                .map(Chart::getId)
                .collect(Collectors.toList());

        for (Long chartId : chartIds) {
            log.info("produce message: {}", chartId);
            producer.sendMessage(chartId.toString());
        }
        log.info("end");

    }

}
