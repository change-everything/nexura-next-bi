package cn.nexura.nextbi.model.dto.chart.inner;

import cn.nexura.nextbi.model.dto.chart.GenChartByAiRequest;
import cn.nexura.nextbi.model.entity.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * 文件上传请求
 *
 * @author 86188
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class InnerGenChartByAiRequest extends GenChartByAiRequest {

    /**
     * 用户
     */
    private User loginUser;

    private static final long serialVersionUID = 1L;
}