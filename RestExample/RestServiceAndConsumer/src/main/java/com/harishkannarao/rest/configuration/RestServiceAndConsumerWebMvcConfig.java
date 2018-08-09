package com.harishkannarao.rest.configuration;

import com.harishkannarao.rest.filter.CustomExceptionSimulationFilter;
import com.harishkannarao.rest.filter.ResponseHeaderFilter;
import com.harishkannarao.rest.interceptor.request.EvilHeaderRequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Collections;

@Configuration
public class RestServiceAndConsumerWebMvcConfig implements WebMvcConfigurer {
    @Autowired
    private EvilHeaderRequestInterceptor evilHeaderRequestInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(evilHeaderRequestInterceptor);
    }

    @Bean("responseHeaderFilter")
    public FilterRegistrationBean<ResponseHeaderFilter> registerResponseHeaderFilter() {
        FilterRegistrationBean<ResponseHeaderFilter> filterRegistrationBean = new FilterRegistrationBean<>(new ResponseHeaderFilter());
        filterRegistrationBean.setName(ResponseHeaderFilter.NAME);
        filterRegistrationBean.setOrder(Ordered.LOWEST_PRECEDENCE);
        filterRegistrationBean.setUrlPatterns(Collections.singletonList(ResponseHeaderFilter.PATH));
        return filterRegistrationBean;
    }

    @Bean("customExceptionSimulationFilter")
    public FilterRegistrationBean<CustomExceptionSimulationFilter> registerCustomExceptionFilterFilter() {
        FilterRegistrationBean<CustomExceptionSimulationFilter> filterRegistrationBean = new FilterRegistrationBean<>(new CustomExceptionSimulationFilter());
        filterRegistrationBean.setName(CustomExceptionSimulationFilter.NAME);
        filterRegistrationBean.setOrder(Ordered.LOWEST_PRECEDENCE);
        filterRegistrationBean.setUrlPatterns(Collections.singletonList(CustomExceptionSimulationFilter.PATH));
        return filterRegistrationBean;
    }
}
