package com.harishkannarao.war;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    @Value("${requireSsl}")
    private boolean requireSsl;
    @Autowired
    @Value("${httpPort}")
    private int httpPort;
    @Autowired
    @Value("${httpsPort}")
    private int httpsPort;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        if(requireSsl) {
            http.requiresChannel().anyRequest().requiresSecure();
            boolean isDefaultPorts = (httpPort == 80 && httpsPort == 443);
            if(!isDefaultPorts) {
                http.portMapper().http(httpPort).mapsTo(httpsPort);
            }
        }
    }
}
