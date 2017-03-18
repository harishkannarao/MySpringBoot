package com.harishkannarao.rest.configuration;

import com.harishkannarao.rest.interceptor.request.EvilHeaderRequestInterceptor;
import com.harishkannarao.rest.interceptor.response.HtmlResponseHeaderHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

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


}
