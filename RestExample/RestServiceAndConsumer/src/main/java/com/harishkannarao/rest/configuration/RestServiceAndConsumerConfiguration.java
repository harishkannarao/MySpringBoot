package com.harishkannarao.rest.configuration;

import com.harishkannarao.rest.filter.CustomExceptionSimulationFilter;
import com.harishkannarao.rest.filter.ResponseHeaderFilter;
import com.harishkannarao.rest.interceptor.request.EvilHeaderRequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.io.Resource;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;
import org.springframework.web.servlet.resource.ResourceResolverChain;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;

@Configuration
public class RestServiceAndConsumerConfiguration {

    @Autowired
    private EvilHeaderRequestInterceptor evilHeaderRequestInterceptor;

    @Bean
    @Qualifier("myRestTemplate")
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(evilHeaderRequestInterceptor);
            }

            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                registry
                        .setOrder(Ordered.LOWEST_PRECEDENCE)
                        .addResourceHandler("/**")
                        .addResourceLocations("classpath:/static/")
                        //first time resolved, that route will always be used from cache
                        .resourceChain(true)
                        .addResolver(new IndexFallbackResourceResolver());
            }
        };
    }

    static class IndexFallbackResourceResolver extends PathResourceResolver {
        @Override
        protected Resource resolveResourceInternal(HttpServletRequest request, String requestPath,
                                                   List<? extends Resource> locations, ResourceResolverChain chain) {
            Resource resource = super.resolveResourceInternal(request, requestPath, locations, chain);
            if(resource==null){
                //try with /index.html
                resource = super.resolveResourceInternal(request, requestPath + "/index.html", locations, chain);
            }
            return resource;
        }
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
