package com.harishkannarao.jdbc.configuration;

import com.harishkannarao.jdbc.filter.CorsFilter;
import com.harishkannarao.jdbc.filter.RequestTracingFilter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.client.RestTemplate;

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

    @Bean("requestTracingFilter")
    public FilterRegistrationBean<RequestTracingFilter> registerRequestTracingFilter() {
        FilterRegistrationBean<RequestTracingFilter> filterRegistrationBean = new FilterRegistrationBean<>(new RequestTracingFilter());
        filterRegistrationBean.setName("requestIdFilterBean");
        filterRegistrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        filterRegistrationBean.setUrlPatterns(Collections.singletonList("/*"));
        return filterRegistrationBean;
    }

    @Bean("corsFilter")
    public FilterRegistrationBean<CorsFilter> corsFilter() {
        FilterRegistrationBean<CorsFilter> filterRegistrationBean = new FilterRegistrationBean<>(new CorsFilter(corsOrigins));
        filterRegistrationBean.setName("corsFilterBean");
        filterRegistrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE+1);
        filterRegistrationBean.setUrlPatterns(Collections.singletonList("/*"));
        return filterRegistrationBean;
    }

}
