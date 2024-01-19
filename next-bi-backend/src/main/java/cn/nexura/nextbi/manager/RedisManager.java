package cn.nexura.nextbi.manager;

import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author PeiYP
 * @since 2024年01月19日 14:01
 */
public class RedisManager {

    private RedisTemplate<String, Object> redisTemplate;

    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        RedisSerializer<String> stringSerializer = new StringRedisSerializer();//序列化为String
        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer();
        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setValueSerializer(serializer);
        redisTemplate.setHashKeySerializer(stringSerializer);
        redisTemplate.setHashValueSerializer(serializer);
        this.redisTemplate = redisTemplate;
    }

    public ValueOperations<String, Object> valueOps() {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        return valueOperations;
    }

    public ListOperations<String, Object> listOps() {
        ListOperations<String, Object> listOperations = redisTemplate.opsForList();
        return listOperations;
    }

    public HashOperations<String, String, Object> hashOps() {
        HashOperations<String, String, Object> hashOperations = redisTemplate.opsForHash();
        return hashOperations;
    }

    public SetOperations<String, Object> setOps() {
        SetOperations<String, Object> setOperations = redisTemplate.opsForSet();
        return setOperations;
    }

}
