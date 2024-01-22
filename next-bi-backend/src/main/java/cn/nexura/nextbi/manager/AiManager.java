package cn.nexura.nextbi.manager;

import cn.nexura.nextbi.common.ErrorCode;
import cn.nexura.nextbi.exception.BusinessException;
import com.github.rholder.retry.*;
import com.yupi.yucongming.dev.client.YuCongMingClient;
import com.yupi.yucongming.dev.common.BaseResponse;
import com.yupi.yucongming.dev.model.DevChatRequest;
import com.yupi.yucongming.dev.model.DevChatResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author PeiYP
 * @since 2024年01月11日 14:56
 */
@Service
@Slf4j
public class AiManager {

    @Resource
    private YuCongMingClient yuCongMingClient;


    // 重试三次
    public String doChat(long modelId, String message) {
        AtomicInteger count = new AtomicInteger();
        // 定义callable接口，用于重试
        Callable<String> callable = () -> {
            log.info("第 {} 次", (count.incrementAndGet()));
            DevChatRequest devChatRequest = new DevChatRequest();
            devChatRequest.setModelId(modelId);
            devChatRequest.setMessage(message);
            BaseResponse<DevChatResponse> response = yuCongMingClient.doChat(devChatRequest);
            if (response == null || response.getCode() != 0) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "AI响应错误");
            }
            return response.getData().getContent();
        };


        // 重试，每隔5分钟，重试一次，一共重试3次
        Retryer<String> retryer = RetryerBuilder.<String>newBuilder()
                .retryIfResult(Objects::isNull)
                .retryIfExceptionOfType(Exception.class)
                .retryIfRuntimeException()
                .withWaitStrategy(WaitStrategies.exponentialWait(100, 5, TimeUnit.MINUTES))
                .withStopStrategy(StopStrategies.stopAfterAttempt(3))
                .build();

        try {
            return retryer.call(callable);
        } catch (ExecutionException | RetryException e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "AI响应错误");
        }

    }

}
