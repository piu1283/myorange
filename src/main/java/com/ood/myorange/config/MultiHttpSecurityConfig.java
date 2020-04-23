package com.ood.myorange.config;

import com.ood.myorange.auth.CustomizeAdminAuthenticationSuccessHandler;
import com.ood.myorange.auth.CustomizeAuthenticationEntryPoint;
import com.ood.myorange.auth.CustomizeAuthenticationFailureHandler;
import com.ood.myorange.auth.CustomizeUserAuthenticationSuccessHandler;
import com.ood.myorange.constant.RoleConstant;
import com.ood.myorange.service.impl.CustomAdminDetailService;
import com.ood.myorange.service.impl.CustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableRedisHttpSession
@Configuration
public class MultiHttpSecurityConfig {

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

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
            http.cors().and()
                    .csrf().disable()
                    .antMatcher("/admin/**")
                    .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)
                    .and()
                    .formLogin()
                    .loginProcessingUrl("/admin/login")
                    .successHandler(authenticationSuccessHandler)
                    .failureHandler(authenticationFailureHandler)
                    .and()
                    .logout()
                    .logoutUrl("/admin/logout")
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
            http.cors().and()
                    .csrf().disable().antMatcher("/api/**")
                    .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)
                    .and()
                    .authorizeRequests().antMatchers("/api/d/**").permitAll()
                    .and()
                    .authorizeRequests()
                    .antMatchers("/api/**").hasRole(RoleConstant.USER.toString())
                    .and()
                    .formLogin()
                    .loginProcessingUrl("/api/login")
                    .successHandler(authenticationSuccessHandler)
                    .failureHandler(authenticationFailureHandler)
                    .and()
                    .logout()
                    .logoutUrl("/api/logout")
                    .defaultLogoutSuccessHandlerFor(new HttpStatusReturningLogoutSuccessHandler(),
                            new AntPathRequestMatcher("/api/logout"))
                    .and();

        }
    }

    @Configuration
    @Order(3)
    public static class OtherSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {

        protected void configure(HttpSecurity http) throws Exception {
            http.cors().and().csrf().disable().authorizeRequests().anyRequest().permitAll();
        }
    }
}
