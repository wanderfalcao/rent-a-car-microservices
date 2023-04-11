package br.com.infnet.wander.filter;

import br.com.infnet.wander.utility.JwtUtil;
import br.com.infnet.wander.utility.RouterValidator;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;




@AllArgsConstructor
@RefreshScope
@Component
public class AuthenticationFilter implements GatewayFilter {

    private JwtUtil jwtUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        boolean test = RouterValidator.isNotSecuredPost.test(request);
        if(!RouterValidator.isNotSecuredPost.test(request)){
            return chain.filter(exchange);
        } else {
            if (this.isAuthMissing(request)) {
                return this.onError(exchange, "Authorization header is missing in request", HttpStatus.UNAUTHORIZED);
            }

            final String token = this.getAuthHeader(request);

            String jwt = token.substring(7);

            if (jwtUtil.isInvalid(jwt))
                return this.onError(exchange, "Authorization header is invalid", HttpStatus.UNAUTHORIZED);

            this.populateRequestWithHeaders(exchange, jwt);

            if (!RouterValidator.isSecuredGetOnlyADMIN.test(request)){
                String role = jwtUtil.getRoles(jwt);
                if(!role.equals("ROLE_ADMIN")) {
                    return this.onError(exchange, "Admin privilege is required", HttpStatus.UNAUTHORIZED);
                }
            }
        }


        return chain.filter(exchange);
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }

    private String getAuthHeader(ServerHttpRequest request) {
        return request.getHeaders().getOrEmpty("Authorization").get(0);
    }

    private boolean isAuthMissing(ServerHttpRequest request) {

        return !request.getHeaders().containsKey("Authorization");
    }

    private void populateRequestWithHeaders(ServerWebExchange exchange, String token) {
        Claims decodedJWT = jwtUtil.decodedJWT(token);


        exchange.getRequest().mutate()
                .header("email", decodedJWT.get("username").toString())
                .build();
    }

}

