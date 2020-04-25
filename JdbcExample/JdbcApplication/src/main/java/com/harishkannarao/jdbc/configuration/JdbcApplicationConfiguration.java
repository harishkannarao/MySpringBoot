package com.harishkannarao.jdbc.configuration;

import com.harishkannarao.jdbc.filter.RequestTracingFilter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Collections;

@Configuration
public class JdbcApplicationConfiguration {

    @Value("${app.cors.origins}")
    private String corsOrigins;

    @Bean
    @Qualifier("myRestTemplate")
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins(corsOrigins.split(",")).allowedMethods("*");
            }
        };
    }

    @Bean("requestTracingFilter")
    public FilterRegistrationBean<RequestTracingFilter> registerRequestTracingFilter() {
        FilterRegistrationBean<RequestTracingFilter> filterRegistrationBean = new FilterRegistrationBean<>(new RequestTracingFilter());
        filterRegistrationBean.setName("requestIdFilterBean");
        filterRegistrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        filterRegistrationBean.setUrlPatterns(Collections.singletonList("/*"));
        return filterRegistrationBean;
    }

}
