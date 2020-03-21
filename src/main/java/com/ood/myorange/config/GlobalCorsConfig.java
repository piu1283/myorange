package com.ood.myorange.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

/**
 * Created by Chen on 3/21/20.
 */
@Configuration
public class GlobalCorsConfig {
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("*");
        config.setAllowCredentials(true);
        config.setAllowedMethods(Arrays.asList("POST", "GET", "DELETE", "PUT", "OPTIONS"));
        config.setAllowedHeaders(Arrays.asList("x-Auth-Token", "content-type", "X-Requested-With", "XMLHttpRequest"));
        config.setExposedHeaders(Arrays.asList("x-Auth-Token", "content-type", "X-Requested-With", "XMLHttpRequest"));

        UrlBasedCorsConfigurationSource configSource = new UrlBasedCorsConfigurationSource();
        configSource.registerCorsConfiguration("/**", config);

        return new CorsFilter(configSource);
    }
}
