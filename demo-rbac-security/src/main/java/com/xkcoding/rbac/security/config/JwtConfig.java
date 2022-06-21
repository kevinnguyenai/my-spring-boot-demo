package com.xkcoding.rbac.security.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>
 * JWT Configuration
 * </p>
 *
 * @author yangkai.shen, kevinnguyenai
 * @date Created in 2018-12-07 13:42
 * @updateTime Updated in 2022-06-21 14:00
 */
@ConfigurationProperties(prefix = "jwt.config")
@Data
public class JwtConfig {
    /**
     * jwt encryption key，Defaults：xkcoding.
     */
    private String key = "xkcoding";

    /**
     * jwtExpired time, default value：600000 {@code 10 minute}.
     */
    private Long ttl = 600000L;

    /**
     * After opening to remember me, JWT expires,skythe default value 604800000
     * {@code 7 day}
     */
    private Long remember = 604800000L;
}
