package com.harishkannarao.jdbc.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfiguration {

    public static final String HEADER_AUTH_SCHEME_KEY = "header-auth";
    public static final String COOKIE_AUTH_SCHEME_KEY = "cookie-auth";

    @Bean
    public OpenAPI customOpenAPI(
            @Value("${app.openapi.url}") String appPublicUrl
    ) {
        return new OpenAPI()
                .servers(
                        List.of(
                                new Server().url(appPublicUrl)
                        )
                )
                .components(
                        new Components()
                                .addSecuritySchemes(HEADER_AUTH_SCHEME_KEY,
                                        new SecurityScheme()
                                                .type(SecurityScheme.Type.APIKEY)
                                                .in(SecurityScheme.In.HEADER)
                                                .name("Authorization")
                                                .description("Example: Bearer HELLO_HEADER")
                                )
                                .addSecuritySchemes(COOKIE_AUTH_SCHEME_KEY,
                                        new SecurityScheme()
                                                .type(SecurityScheme.Type.APIKEY)
                                                .in(SecurityScheme.In.COOKIE)
                                                .name("session_cookie")
                                                .description("Example: HELLO_COOKIE")
                                )
                );
    }
}
