package com.maike.winter.redis.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author maike
 * @version 1.0.0
 * @date 2020-01-06 09:26
 */

@Data
@ConfigurationProperties("redis.distribute")
public class LockProperties {

    private String lockPre;
    private int expiredTime;
    private int retryCount;
}
