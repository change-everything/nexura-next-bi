package cn.nexura.nextbi.mapper;

import cn.nexura.nextbi.model.entity.Chart;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
* @author 86188
* @description 针对表【chart(图表信息表)】的数据库操作Mapper
* @createDate 2024-01-10 16:33:02
* @Entity cn.nexura.nextbi.model.entity.Chart
*/
@Mapper
public interface ChartMapper extends BaseMapper<Chart> {

    boolean createTable(@Param("tableField") List<String> tableField, @Param("chartId") Long chartId);

    boolean insertDataBatch(@Param("tableData") List<String[]> tableData, @Param("chartId") Long chartId);

    List<Map<String, String>> getChartDataByChartId(@Param("chartId") Long chartId);
}




