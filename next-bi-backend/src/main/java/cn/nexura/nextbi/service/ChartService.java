package cn.nexura.nextbi.service;

import cn.nexura.nextbi.model.dto.chart.ChartQueryRequest;
import cn.nexura.nextbi.model.entity.User;
import cn.nexura.nextbi.model.vo.BiResponse;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.nexura.nextbi.model.entity.Chart;
import org.springframework.web.multipart.MultipartFile;

/**
* @author 86188
* @description 针对表【chart(图表信息表)】的数据库操作Service
* @createDate 2024-01-10 16:33:02
*/
public interface ChartService extends IService<Chart> {


     QueryWrapper<Chart> getQueryWrapper(ChartQueryRequest chartQueryRequest);

     BiResponse doGenChart(MultipartFile multipartFile, User loginUser, String goal, String name, String chartType);


     BiResponse doGenChartAsync(MultipartFile multipartFile, User loginUser, String goal, String name, String chartType);

     String getChartData(Chart chart);
}
