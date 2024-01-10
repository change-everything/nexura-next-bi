package cn.nexura.nextbi.model.dto.chart;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 更新请求
 *
 */
@Data
public class ChartUpdateRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 分析目标
     */
    private String goal;

    /**
     * 图表数据
     */
    private String chartData;

    /**
     * 图表类型
     */
    private String chartType;


    private static final long serialVersionUID = 1L;
}