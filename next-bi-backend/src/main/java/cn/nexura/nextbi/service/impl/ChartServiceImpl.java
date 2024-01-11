package cn.nexura.nextbi.service.impl;

import cn.nexura.nextbi.common.ErrorCode;
import cn.nexura.nextbi.constant.CommonConstant;
import cn.nexura.nextbi.exception.BusinessException;
import cn.nexura.nextbi.mapper.ChartMapper;
import cn.nexura.nextbi.model.dto.chart.ChartQueryRequest;
import cn.nexura.nextbi.model.dto.user.UserQueryRequest;
import cn.nexura.nextbi.model.entity.User;
import cn.nexura.nextbi.service.ChartService;
import cn.nexura.nextbi.utils.SqlUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.nexura.nextbi.model.entity.Chart;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
* @author 86188
* @description 针对表【chart(图表信息表)】的数据库操作Service实现
* @createDate 2024-01-10 16:33:02
*/
@Service
public class ChartServiceImpl extends ServiceImpl<ChartMapper, Chart>
    implements ChartService {


    @Override
    public QueryWrapper<Chart> getQueryWrapper(ChartQueryRequest chartQueryRequest) {
             if (chartQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }

        Long id = chartQueryRequest.getId();
        String goal = chartQueryRequest.getGoal();
        String chartType = chartQueryRequest.getChartType();
        String name = chartQueryRequest.getName();
        Long userId = chartQueryRequest.getUserId();
        String sortField = chartQueryRequest.getSortField();
        String sortOrder = chartQueryRequest.getSortOrder();

        QueryWrapper<Chart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(id != null && id > 0, "id", id);
        queryWrapper.eq(StringUtils.isNotBlank(goal), "goal", goal);
        queryWrapper.eq(StringUtils.isNotBlank(name), "name", name);
        queryWrapper.eq(StringUtils.isNotBlank(chartType), "chart_type", chartType);
        queryWrapper.eq(userId != null && userId > 0, "user_id", userId);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }
}




