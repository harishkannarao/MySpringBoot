package com.harishkannarao.rest.configuration;

import com.harishkannarao.rest.filter.ResponseHeaderFilter;
import com.harishkannarao.rest.interceptor.request.EvilHeaderRequestInterceptor;
import com.harishkannarao.rest.interceptor.response.HtmlResponseHeaderHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import static java.util.Arrays.asList;
import static org.springframework.boot.web.servlet.FilterRegistrationBean.REQUEST_WRAPPER_FILTER_MAX_ORDER;

@Configuration
public class RestServiceAndConsumerWebMvcConfig extends WebMvcConfigurerAdapter {
    @Autowired
    private EvilHeaderRequestInterceptor evilHeaderRequestInterceptor;
    @Autowired
    private HtmlResponseHeaderHandler htmlResponseHeaderHandler;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(evilHeaderRequestInterceptor);
        registry.addInterceptor(htmlResponseHeaderHandler);
    }

    @Bean("responseHeaderFilter")
    public FilterRegistrationBean registerResponseHeaderFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new ResponseHeaderFilter());
        filterRegistrationBean.setName(ResponseHeaderFilter.NAME);
        filterRegistrationBean.setOrder(REQUEST_WRAPPER_FILTER_MAX_ORDER);
        filterRegistrationBean.setUrlPatterns(asList(ResponseHeaderFilter.PATH));
        return filterRegistrationBean;
    }

}
