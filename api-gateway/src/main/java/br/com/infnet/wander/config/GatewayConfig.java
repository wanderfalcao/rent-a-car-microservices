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
                .route("OPENING-HOURS", r -> r.path("/api/v1/opening-hours/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://OPENING-HOURS-SERVICE/"))

                .route("AUTHENTICATION", r -> r.path("/api/v1/auth/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://AUTHENTICATION-SERVICE/"))

                .route("LOCATION", r -> r.path("/api/v1/location/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://LOCATION-SERVICE/"))

                .route("CAR", r -> r.path("/api/v1/car/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://CAR-SERVICE/"))

                .route("ORDER", r -> r.path("/api/v1/order/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://ORDER-SERVICE/"))

                .route("CURRENCY", r -> r.path("/api/v1/currency/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://CURRENCY-SERVICE/"))

                .build();
    }

}
