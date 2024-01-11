package cn.nexura.nextbi.model.dto.chart;

import lombok.Data;

import java.io.Serializable;

/**
 * 文件上传请求
 *
 * @author 86188
 */
@Data
public class GenChartByAiRequest implements Serializable {

    /**
     * 分析目标
     */
    private String goal;

    /**
     * 名称
     */
    private String name;

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