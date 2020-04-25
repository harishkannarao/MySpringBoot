package com.harishkannarao.jdbc.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfiguration {

    @Bean
    public OpenAPI customOpenAPI(
            @Value("${app.openapi.url}") String appPublicUrl
    ) {
        return new OpenAPI()
                .servers(
                        List.of(
                                new Server().url(appPublicUrl)
                        )
                );
    }
}
