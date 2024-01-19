package cn.nexura.nextbi.manager;

import cn.nexura.nextbi.common.ErrorCode;
import cn.nexura.nextbi.exception.BusinessException;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author PeiYP
 * @since 2024年01月12日 15:49
 */
@Service
public class RedisLimiterManager {

    @Resource
    private RedissonClient redissonClient;

    /**
     * 限流
     * @param key
     */
    public void doRateLimit(String key) {
        RRateLimiter rateLimiter = redissonClient.getRateLimiter(key);
        rateLimiter.trySetRate(RateType.OVERALL, 2, 1, RateIntervalUnit.SECONDS);
        boolean canOption = rateLimiter.tryAcquire(1);
        if (!canOption) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
    }

}
