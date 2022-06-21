package com.xkcoding.rbac.security.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>
 * Custom configuration
 * </p>
 *
 * @author yangkai.shen, kevinnguyenai
 * @date Created in 2018-12-13 10:56
 * @updateTime Updated in 2022-06-20 14:00
 */
@ConfigurationProperties(prefix = "custom.config")
@Data
public class CustomConfig {
    /**
     * The address that does not need to be intercepted
     */
    private IgnoreConfig ignores;
}
