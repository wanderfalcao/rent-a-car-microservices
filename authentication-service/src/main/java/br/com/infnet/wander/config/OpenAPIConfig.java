package br.com.infnet.wander.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI apiInfo() {

        final Server server = new Server();
        server.setUrl("http://localhost:8085/");

        final List<Server> servers = new ArrayList<>();
        servers.add(server);

        return new OpenAPI()

                .servers(
                        new ArrayList<>(
                                servers
                        )
                )
                .info(
                        new Info()
                                .title("Authentication service")
                                .description(
                                        "This is the authentication service for the Rent a Car Software System")
                                .version("1.0.0")
                );
    }
}
