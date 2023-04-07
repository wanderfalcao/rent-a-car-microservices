package br.com.infnet.wander.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class OpenAPIConfig {
    
    @Bean
    public OpenAPI apiInfo() {
        final String securitySchemeName = "bearerAuth";

        final Server server = new Server();
        server.setUrl("http://localhost:8084/");
        
        final List<Server> servers = new ArrayList<>();
        servers.add(server);
        
        return new OpenAPI()
                .components(
                        new Components()
                                .addSecuritySchemes(securitySchemeName,
                                        new SecurityScheme()
                                                .name(securitySchemeName)
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")
                                )
                )
                .servers(
                        new ArrayList<>(
                                servers
                        )
                )
                .info(
                        new Info()
                                .title("Order Service")
                                .description(
                                        "This is the order service for the Rent-a-car microservice architecture")
                                .version("1.0.0")
                );
    }
}
