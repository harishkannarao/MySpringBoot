package com.harishkannarao.jdbc.configuration;

import com.harishkannarao.jdbc.client.interceptor.RestTemplateAccessLoggingInterceptor;
import com.harishkannarao.jdbc.filter.RequestTracingFilter;
import com.harishkannarao.jdbc.interceptor.AuthHeaderRequestInterceptor;
import com.harishkannarao.jdbc.interceptor.CookieRequestInterceptor;
import com.harishkannarao.jdbc.interceptor.SubjectRoleInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

@Configuration
public class JdbcApplicationConfiguration {

    private static final long DEFAULT_CONNECT_TIMEOUT_MS = 3000;
    private static final long DEFAULT_READ_TIMEOUT_MS = 15000;

    @Value("${app.cors.origins}")
    private String corsOrigins;
    @Autowired
    private AuthHeaderRequestInterceptor authHeaderRequestInterceptor;
    @Autowired
    private CookieRequestInterceptor cookieRequestInterceptor;
    @Autowired
    private SubjectRoleInterceptor subjectRoleInterceptor;

    @Bean
    @Qualifier("myRestTemplate")
    public RestTemplate getRestTemplate(RestTemplateBuilder restTemplateBuilder) {
        RestTemplateAccessLoggingInterceptor restTemplateAccessLoggingInterceptor = new RestTemplateAccessLoggingInterceptor();

        BufferingClientHttpRequestFactory clientHttpRequestFactory = new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory());

        return restTemplateBuilder
                .setConnectTimeout(Duration.ofMillis(DEFAULT_CONNECT_TIMEOUT_MS))
                .setReadTimeout(Duration.ofMillis(DEFAULT_READ_TIMEOUT_MS))
                .requestFactory(() -> clientHttpRequestFactory)
                .additionalInterceptors(List.of(restTemplateAccessLoggingInterceptor))
                .build();
    }

    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins(corsOrigins.split(",")).allowedMethods("*");
            }

            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                // Interceptors are called in the same order it is added to the registry
                registry.addInterceptor(authHeaderRequestInterceptor);
                registry.addInterceptor(cookieRequestInterceptor);
                registry.addInterceptor(subjectRoleInterceptor);
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
