package cn.nexura.nextbi.controller;

import cn.hutool.core.io.FileUtil;
import cn.nexura.nextbi.annotation.AuthCheck;
import cn.nexura.nextbi.common.BaseResponse;
import cn.nexura.nextbi.common.DeleteRequest;
import cn.nexura.nextbi.common.ErrorCode;
import cn.nexura.nextbi.common.ResultUtils;
import cn.nexura.nextbi.constant.UserConstant;
import cn.nexura.nextbi.exception.BusinessException;
import cn.nexura.nextbi.exception.ThrowUtils;
import cn.nexura.nextbi.manager.RedisLimiterManager;
import cn.nexura.nextbi.model.dto.chart.*;
import cn.nexura.nextbi.model.dto.chart.inner.InnerGenChartByAiRequest;
import cn.nexura.nextbi.model.entity.Chart;
import cn.nexura.nextbi.model.entity.User;
import cn.nexura.nextbi.model.vo.BiResponse;
import cn.nexura.nextbi.service.ChartService;
import cn.nexura.nextbi.service.UserService;
import cn.nexura.nextbi.sse.service.SseService;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 帖子接口,外部api调用
 *
 * @author peiYP
 */
@RestController
@RequestMapping("/api/chart")
@Slf4j
public class ApiController {

    @Resource
    private ChartService chartService;

    @Resource
    private UserService userService;


    @Resource
    private RedisLimiterManager redisLimiterManager;


    /**
     * 智能分析 异步
     *
     * @param multipartFile
     * @return
     */
    @PostMapping("/genAsync")
    public BaseResponse<BiResponse> genChartByAiAsync(@RequestPart("file") MultipartFile multipartFile, InnerGenChartByAiRequest genChartByAiRequest) {


        if (genChartByAiRequest.getLoginUser() == null) {
            throw new BusinessException(ErrorCode.FORBIDDEN_ERROR, "鉴权失败");
        }


        long size = multipartFile.getSize();
        ThrowUtils.throwIf(size > 1024 * 1024L, ErrorCode.PARAMS_ERROR, "文件过大");

        String originalFilename = multipartFile.getOriginalFilename();
        String suffix = FileUtil.getSuffix(originalFilename);
        List<String> validSuffixList = Arrays.asList("xlsx", "csv", "xlx");
        ThrowUtils.throwIf(!validSuffixList.contains(suffix), ErrorCode.PARAMS_ERROR, "文件格式不正确");

        String goal = genChartByAiRequest.getGoal();
        String name = genChartByAiRequest.getName();
        String chartType = genChartByAiRequest.getChartType();

        // 校验
        ThrowUtils.throwIf(StringUtils.isBlank(goal), ErrorCode.PARAMS_ERROR, "目标为空");
        ThrowUtils.throwIf(StringUtils.isNotBlank(name) && name.length() > 100, ErrorCode.PARAMS_ERROR, "名称过长");


        // 限流操作
        redisLimiterManager.doRateLimit("genChartByAi_" + genChartByAiRequest.getLoginUser().getId());

        BiResponse biResponse = chartService.doGenChartAsync(multipartFile, genChartByAiRequest.getLoginUser(), goal, name, chartType, 0L, null);

        return ResultUtils.success(biResponse);
    }


    /**
     * 重新生成
     */
    @PostMapping("/reGenAsync")
    public BaseResponse<BiResponse> reGenChartByAiAsync(@RequestBody InnerGenChartByAiRequest genChartByAiRequest) {


        if (genChartByAiRequest.getLoginUser() == null) {
            throw new BusinessException(ErrorCode.FORBIDDEN_ERROR, "鉴权失败");
        }


        String goal = genChartByAiRequest.getGoal();
        String name = genChartByAiRequest.getName();
        String chartType = genChartByAiRequest.getChartType();
        List<List<String>> chartData = genChartByAiRequest.getChartData();
        String genResult = genChartByAiRequest.getGenResult();

        // 校验
        ThrowUtils.throwIf(StringUtils.isBlank(goal), ErrorCode.PARAMS_ERROR, "目标为空");
        ThrowUtils.throwIf(StringUtils.isNotBlank(name) && name.length() > 100, ErrorCode.PARAMS_ERROR, "名称过长");


        // 限流操作
        redisLimiterManager.doRateLimit("genChartByAi_" + genChartByAiRequest.getLoginUser().getId());

        BiResponse biResponse = chartService.doGenChartAsync(null, genChartByAiRequest.getLoginUser(), goal, name, chartType, genChartByAiRequest.getId(), genResult);

        return ResultUtils.success(biResponse);
    }


}
