package com.harishkannarao.jdbc.configuration;

import ch.qos.logback.access.tomcat.LogbackValve;
import org.springframework.boot.tomcat.servlet.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LogbackAccessConfig {

	@Bean
	public WebServerFactoryCustomizer<TomcatServletWebServerFactory> tomcatCustomizer() {
		return factory -> {
			LogbackValve valve = new LogbackValve();
			valve.setFilename("logback-access.xml");
			factory.addContextValves(valve);
		};
	}

}
