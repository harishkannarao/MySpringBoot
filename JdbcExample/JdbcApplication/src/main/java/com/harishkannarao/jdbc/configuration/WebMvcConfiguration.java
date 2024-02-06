package com.harishkannarao.jdbc.configuration;

import com.harishkannarao.jdbc.filter.RequestTracingFilter;
import com.harishkannarao.jdbc.interceptor.AuthHeaderRequestInterceptor;
import com.harishkannarao.jdbc.interceptor.CookieRequestInterceptor;
import com.harishkannarao.jdbc.interceptor.SubjectRoleInterceptor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Collections;

@Configuration
public class WebMvcConfiguration {
	@Bean
	public WebMvcConfigurer webMvcConfigurer(
		@Value("${app.cors.origins}") String corsOrigins,
		AuthHeaderRequestInterceptor authHeaderRequestInterceptor,
		CookieRequestInterceptor cookieRequestInterceptor,
		SubjectRoleInterceptor subjectRoleInterceptor) {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(@NotNull CorsRegistry registry) {
				registry.addMapping("/**").allowedOrigins(corsOrigins.split(",")).allowedMethods("*");
			}

			@Override
			public void addInterceptors(@NotNull InterceptorRegistry registry) {
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
		filterRegistrationBean.setName(RequestTracingFilter.NAME);
		filterRegistrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
		filterRegistrationBean.setUrlPatterns(Collections.singletonList(RequestTracingFilter.PATH));
		return filterRegistrationBean;
	}

}
