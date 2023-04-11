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

    public static final List<String> openApiEndpointsPostNotSecured= List.of(
            "/api/v1/auth/signin",
            "/api/v1/auth/signup"

    );

    public static final List<String> openApiEndpointsGetSecuredOnlyADMIN= List.of(
           "/api/v1/order/getAll"

    );
    public static final List<String> openApiEndpointsPostSecuredOnlyADMIN= List.of(
            "/api/v1/car/"

    );
    public static final List<String> openApiEndpointsDeleteSecuredOnlyADMIN= List.of(
            "/api/v1/car/"

    );


    public static final Predicate<ServerHttpRequest> isNotSecuredPost =
            request -> openApiEndpointsPostNotSecured
                    .stream()
                    .noneMatch(uri -> request.getPath().toString().equals(uri)
                            && Objects.equals(request.getMethod(), HttpMethod.POST));
    public static final Predicate<ServerHttpRequest> isSecuredGetOnlyADMIN =
            request -> openApiEndpointsGetSecuredOnlyADMIN
                    .stream()
                    .noneMatch(uri -> request.getPath().toString().equals(uri)
                            && Objects.equals(request.getMethod(), HttpMethod.GET));
}
