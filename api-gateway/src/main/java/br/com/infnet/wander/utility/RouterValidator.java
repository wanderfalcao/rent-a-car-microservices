package br.com.infnet.wander.utility;

import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

@Component
public class RouterValidator {

    public static final List<String> openApiEndpointsGet = List.of(
            "/api/v1/car/**",
            "/api/v1/car",
            "/api/v1/car/findByStatus",
            "/api/v1/currency",
            "/api/v1/currency/**",
            "/api/v1/opening-hours",
            "/api/v1/opening-hours/**",
            "/api/v1/location/**",
            "/api/v1/location"
    );

    public static final List<String> openApiEndpointsPost= List.of(
            "/api/v1/order",
            "/api/v1/auth/admin",
            "/api/v1/auth/order"

    );

    public static final Predicate<ServerHttpRequest> isSecuredGet =
            request -> openApiEndpointsGet
                    .stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri) && Objects.equals(request.getMethod(), HttpMethod.GET)
                    );

    public static final Predicate<ServerHttpRequest> isSecuredPost =
            request -> openApiEndpointsPost
                    .stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri) && Objects.equals(request.getMethod(), HttpMethod.POST)
                    );

}
