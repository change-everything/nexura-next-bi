package cn.nexura.nextbi.config;

import lombok.Data;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author PeiYP
 * @since 2024年01月12日 15:33
 */
@Configuration
@ConfigurationProperties(prefix = "spring.redis")
@Data
public class RedissonConfig {

    private Integer database;

    private String host;

    private Integer port;

    private String password;


    @Bean
    public RedissonClient redissonClient() {

        Config config = new Config();
        config.useSingleServer()
                .setDatabase(2)
                .setAddress("redis://" + host + ":" + port)
                .setPassword(password);

        RedissonClient redissonClient = Redisson.create(config);
        return redissonClient;
    }

}
