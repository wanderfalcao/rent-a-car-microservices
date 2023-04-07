package br.com.infnet.wander.utility;

import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;

@Component
public class RouterValidator {
//
// "/api/v1/car/**",
//         "/api/v1/car",
//         "/api/v1/car/findByStatus",
//         "/api/v1/currency",
//         "/api/v1/currency/**",
//         "/api/v1/opening-hours",
//         "/api/v1/opening-hours/**",
//         "/api/v1/location/**",
//         "/api/v1/location"
    public static final List<String> openApiEndpointsSecuredGet = List.of(
        "/api/v1/order/",
        "/api/v1/car"
    );

    public static final List<String> openApiEndpointsSecuredPost = List.of(
            "/api/v1/order/"


    );

    public static final List<String> openApiEndpointsPostNotSecured= List.of(
            "/api/v1/auth/signin",
            "/api/v1/auth/signup"

    );

    public static final List<String> openApiEndpointsGetSecuredOnlyADMIN= List.of(
           "/api/v1/order/getAll"

    );

    public static final Predicate<ServerHttpRequest> isSecuredGet =
            request -> openApiEndpointsSecuredGet
                    .stream()
                    .noneMatch(uri -> request.getPath().toString().equals(uri)
                            && Objects.equals(request.getMethod(), HttpMethod.GET));

    public static final Predicate<ServerHttpRequest> isNotSecuredPost =
            request -> openApiEndpointsPostNotSecured
                    .stream()
                    .noneMatch(uri -> request.getPath().toString().equals(uri)
                            && Objects.equals(request.getMethod(), HttpMethod.GET));

    public static final Predicate<ServerHttpRequest> isSecuredPost =
            request -> openApiEndpointsSecuredPost
                    .stream()
                    .noneMatch(uri -> request.getPath().toString().equals(uri)
                            && Objects.equals(request.getMethod(), HttpMethod.GET));
    public static final Predicate<ServerHttpRequest> isSecuredGetOnlyADMIN =
            request -> openApiEndpointsGetSecuredOnlyADMIN
                    .stream()
                    .noneMatch(uri -> request.getPath().toString().equals(uri)
                            && Objects.equals(request.getMethod(), HttpMethod.GET));
}
