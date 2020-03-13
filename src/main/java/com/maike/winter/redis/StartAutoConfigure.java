package com.maike.winter.redis;

import com.maike.winter.redis.config.LockProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author maike
 * @version 1.0.0
 * @date 2020-03-12 11:29
 */
@Configuration
@EnableConfigurationProperties(LockProperties.class)
@ComponentScan(basePackages = {"com.miake.winter.redis"})
public class StartAutoConfigure {



}
