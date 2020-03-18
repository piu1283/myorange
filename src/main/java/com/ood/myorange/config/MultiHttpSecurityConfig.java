package com.ood.myorange.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ood.myorange.auth.CustomizeAdminAuthenticationSuccessHandler;
import com.ood.myorange.auth.CustomizeAuthenticationEntryPoint;
import com.ood.myorange.auth.CustomizeAuthenticationFailureHandler;
import com.ood.myorange.auth.CustomizeUserAuthenticationSuccessHandler;
import com.ood.myorange.constant.RoleConstant;
import com.ood.myorange.filter.ShareKeyFilter;
import com.ood.myorange.service.impl.CustomAdminDetailService;
import com.ood.myorange.service.impl.CustomUserDetailService;
import com.ood.myorange.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.HeaderHttpSessionIdResolver;
import org.springframework.session.web.http.HttpSessionIdResolver;

import javax.annotation.Resource;

/**
 * Created by Chen on 3/17/20.
 */
@EnableWebSecurity
@EnableRedisHttpSession
@Configuration
public class MultiHttpSecurityConfig {

    @Configuration
    @Order(1)
    public static class AdminSecurityConfig extends WebSecurityConfigurerAdapter {
        @Resource(name = "adminDetailService")
        CustomAdminDetailService adminDetailService;

        @Autowired
        CustomizeAuthenticationEntryPoint authenticationEntryPoint;

        @Autowired
        CustomizeAdminAuthenticationSuccessHandler authenticationSuccessHandler;

        @Autowired
        CustomizeAuthenticationFailureHandler authenticationFailureHandler;

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(adminDetailService);
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.csrf().disable()
                    .antMatcher("/admin/**")
                    .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)
                    .and()
                    .formLogin()
                    .loginProcessingUrl("/admin/login")
                    .successHandler(authenticationSuccessHandler)
                    .failureHandler(authenticationFailureHandler)
                    .and()
                    .logout()
                    .defaultLogoutSuccessHandlerFor(new HttpStatusReturningLogoutSuccessHandler(),
                            new AntPathRequestMatcher("/admin/logout"))
                    .and()
                    .authorizeRequests()
                    .antMatchers("/admin/**").hasRole(RoleConstant.ADMIN.toString());
        }

        @Bean
        public BCryptPasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }

        @Bean
        public LettuceConnectionFactory connectionFactory() {
            return new LettuceConnectionFactory();
        }

        @Bean
        public HttpSessionIdResolver httpSessionIdResolver() {
            return HeaderHttpSessionIdResolver.xAuthToken();
        }
    }

    @Configuration
    @Order(2)
    public static class UserSecurityConfig extends WebSecurityConfigurerAdapter {

        @Resource(name = "userDetailService")
        CustomUserDetailService userDetailService;

        @Autowired
        CustomizeAuthenticationEntryPoint authenticationEntryPoint;

        @Autowired
        CustomizeUserAuthenticationSuccessHandler authenticationSuccessHandler;

        @Autowired
        CustomizeAuthenticationFailureHandler authenticationFailureHandler;

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(userDetailService);
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.csrf().disable().antMatcher("/api/**")
                    .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)
                    .and()
                    .formLogin()
                    .loginProcessingUrl("/api/login")
                    .successHandler(authenticationSuccessHandler)
                    .failureHandler(authenticationFailureHandler)
                    .and()
                    .logout()
                    .defaultLogoutSuccessHandlerFor(new HttpStatusReturningLogoutSuccessHandler(),
                            new AntPathRequestMatcher("/api/logout"))
                    .and()
                    .authorizeRequests()
                    .antMatchers("/api/**").hasAuthority().hasRole(RoleConstant.USER.toString());
        }
    }

    @Configuration
    @Order(3)
    public static class OtherSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {

        @Autowired
        CustomizeAuthenticationEntryPoint authenticationEntryPoint;

        @Autowired
        ObjectMapper objectMapper;

        @Autowired
        RedisUtil redisUtil;

        protected void configure(HttpSecurity http) throws Exception {
            http.csrf().disable().antMatcher("/s/**")
                    .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)
                    .and()
                    .addFilterBefore(new ShareKeyFilter(redisUtil, objectMapper), UsernamePasswordAuthenticationFilter.class);
        }
    }
}
