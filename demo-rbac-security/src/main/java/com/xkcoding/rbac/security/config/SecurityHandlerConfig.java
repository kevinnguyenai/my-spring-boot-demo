package com.xkcoding.rbac.security.config;

import com.xkcoding.rbac.security.common.Status;
import com.xkcoding.rbac.security.util.ResponseUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.access.AccessDeniedHandler;

/**
 * <p>
 * Security Result processing configuration
 * </p>
 *
 * @author yangkai.shen, kevinnguyenai
 * @date Created in 2018-12-07 17:31
 * @updateTime Updated in 2022-06-14 15:00
 */
@Configuration
public class SecurityHandlerConfig {

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> ResponseUtil.renderJson(response, Status.ACCESS_DENIED,
                null);
    }

}
