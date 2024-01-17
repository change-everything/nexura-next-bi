package cn.nexura.nextbi.service.impl;

import cn.nexura.nextbi.model.entity.Chart;
import cn.nexura.nextbi.service.ChartService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ChartServiceImplTest {

    @Resource
    private ChartService chartService;

    @Test
    void getChartData() {

        Chart chart = new Chart();
        chart.setId(123456789L);
        String chartData = chartService.getChartData(chart);
        System.out.println(chartData);

    }
}