package br.com.infnet.wander.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class OpenAPIConfig {
    
    
    @Bean
    public OpenAPI apiInfo() {
        final String securitySchemeName = "bearerAuth";
        
        final Server carServer = new Server();
        carServer.setUrl("http://localhost:8083/");

        final Server orderServer = new Server();
        orderServer.setUrl("http://localhost:8084/");

        final Server authServer = new Server();
        orderServer.setUrl("http://localhost:8085/");
        
        final List<Server> servers = new ArrayList<>();
        servers.add(authServer);
        
        
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
                                .title("Car Service")
                                .description(
                                        "This is the car service for the Rent a Car Software System.")
                                .version("1.0.0")
                );
    }
}
