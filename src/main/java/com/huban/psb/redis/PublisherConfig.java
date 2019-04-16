package com.huban.psb.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @ClassName PublisherConfig
 * @Description TODO
 * Author huihui
 * Date 19-4-12 上午10:33
 * Version 1.0
 */
@Configuration
public class PublisherConfig {

    @Bean
    public StringRedisTemplate template(RedisConnectionFactory connectionFactory){
        return new StringRedisTemplate(connectionFactory);
    }
}
