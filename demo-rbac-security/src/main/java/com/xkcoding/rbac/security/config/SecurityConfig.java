package com.xkcoding.rbac.security.config;

import com.xkcoding.rbac.security.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * <p>
 * Security Configuration
 * </p>
 *
 * @author yangkai.shen, kevinnguyenai
 * @date Created in 2018-12-07 16:46
 * @updateTime Updated in 2022-06-21 14:00
 */
@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(CustomConfig.class)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private CustomConfig customConfig;

    @Autowired
    private AccessDeniedHandler accessDeniedHandler;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService).passwordEncoder(encoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // @formatter:off
        http.cors()
                // closure CSRF
                .and().csrf().disable()
                // Login behavior is realized by yourself, refer to it AuthController#login
                .formLogin().disable()
                .httpBasic().disable()

                // Certification request
                .authorizeRequests()
                // All requests need to be logged in to access
                .anyRequest()
                .authenticated()
                // RBAC dynamics url Certification
                .anyRequest()
                .access("@rbacAuthorityService.hasPermission(request,authentication)")

                // Realize by yourself, please refer to it AuthController#logout
                .and().logout().disable()
                // Session manage
                .sessionManagement()
                // Because of the use JWT，So do not manage here Session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                // Abnormal treatment
                .and().exceptionHandling().accessDeniedHandler(accessDeniedHandler);
        // @formatter:on

        // Add customfilterT filter
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }

    /**
     * Play all requests that can be accessed without logging in, see AuthController
     * It can also be {@link #configure(HttpSecurity)} Medium configuration
     * {@code http.authorizeRequests().antMatchers("/api/auth/**").permitAll()}
     */
    @Override
    public void configure(WebSecurity web) {
        WebSecurity and = web.ignoring().and();

        // neglect GET
        customConfig.getIgnores().getGet().forEach(url -> and.ignoring().antMatchers(HttpMethod.GET, url));

        // neglect POST
        customConfig.getIgnores().getPost().forEach(url -> and.ignoring().antMatchers(HttpMethod.POST, url));

        // neglect DELETE
        customConfig.getIgnores().getDelete().forEach(url -> and.ignoring().antMatchers(HttpMethod.DELETE, url));

        // neglect PUT
        customConfig.getIgnores().getPut().forEach(url -> and.ignoring().antMatchers(HttpMethod.PUT, url));

        // neglect HEAD
        customConfig.getIgnores().getHead().forEach(url -> and.ignoring().antMatchers(HttpMethod.HEAD, url));

        // neglect PATCH
        customConfig.getIgnores().getPatch().forEach(url -> and.ignoring().antMatchers(HttpMethod.PATCH, url));

        // neglect OPTIONS
        customConfig.getIgnores().getOptions().forEach(url -> and.ignoring().antMatchers(HttpMethod.OPTIONS, url));

        // neglect TRACE
        customConfig.getIgnores().getTrace().forEach(url -> and.ignoring().antMatchers(HttpMethod.TRACE, url));

        // Councing according to the request format
        customConfig.getIgnores().getPattern().forEach(url -> and.ignoring().antMatchers(url));

    }
}
