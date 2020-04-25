package com.harishkannarao.jdbc.configuration;

import com.harishkannarao.jdbc.filter.RequestTracingFilter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Configuration
public class JdbcApplicationConfiguration {

    @Bean
    @Qualifier("myRestTemplate")
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @Bean("requestTracingFilter")
    public FilterRegistrationBean<RequestTracingFilter> registerRequestTracingFilter() {
        FilterRegistrationBean<RequestTracingFilter> filterRegistrationBean = new FilterRegistrationBean<>(new RequestTracingFilter());
        filterRegistrationBean.setName(RequestTracingFilter.NAME);
        filterRegistrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        filterRegistrationBean.setUrlPatterns(Collections.singletonList(RequestTracingFilter.PATH));
        return filterRegistrationBean;
    }

}
