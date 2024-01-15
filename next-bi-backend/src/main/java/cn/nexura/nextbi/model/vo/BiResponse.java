package cn.nexura.nextbi.model.vo;

import lombok.Builder;
import lombok.Data;

/**
 * @author PeiYP
 * @since 2024年01月11日 15:36
 */
@Data
@Builder
public class BiResponse {

    private String genChart;

    private String genResult;

    private Long chartId;

    private String chartData;
}
