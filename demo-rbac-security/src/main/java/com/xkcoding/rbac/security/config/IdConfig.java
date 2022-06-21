package com.xkcoding.rbac.security.config;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * Snowflake main key generator
 * </p>
 *
 * @author yangkai.shen, kevinnguyenai
 * @date Created in 2018-12-10 11:28
 * @updateTime Updated in 2022-06-20 14:00
 */
@Configuration
public class IdConfig {
    /**
     * Snow plant
     */
    @Bean
    public Snowflake snowflake() {
        return IdUtil.createSnowflake(1, 1);
    }
}
