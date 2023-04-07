package br.com.infnet.wander.config;

import br.com.infnet.wander.filter.AuthenticationFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    AuthenticationFilter filter;

    public GatewayConfig(AuthenticationFilter filter) {
        this.filter = filter;
    }

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("AUTHENTICATION", r -> r.path("/api/v1/auth/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://AUTHENTICATION-SERVICE/"))

                .route("CAR", r -> r.path("/api/v1/car/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://CAR-SERVICE/"))

                .route("ORDER", r -> r.path("/api/v1/order/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://ORDER-SERVICE/"))
                .route("swagger-ui",
                        r -> r.path("/swagger-ui/**")
                                .uri("classpath:/META-INF/resources/webjars/springdoc-openapi-ui/"))
                // Rota para o Swagger API docs
                .route("swagger-docs",
                        r -> r.path("/v3/api-docs/**")
                                .filters(f -> f.rewritePath("/v3/api-docs", "/v3/api-docs.yaml"))
                                .uri("lb://API-GATEWAY/"))
                .build();
    }

}
