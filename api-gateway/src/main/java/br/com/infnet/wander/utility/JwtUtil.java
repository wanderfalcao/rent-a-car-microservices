package br.com.infnet.wander.utility;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.SignatureException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Value("${rent-a-car.jwt-secret}")
    private String secret;
    public static final String ROLES = "role";
    private static final Logger log = LoggerFactory.getLogger(JwtUtil.class);
    public Claims decodedJWT(String token) {
        byte[] secretBytes = secret.getBytes();

        return Jwts.parser()
                .setSigningKey(secretBytes)
                .parseClaimsJws(token)
                .getBody();

    }
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = decodedJWT(token);
        return claimsResolver.apply(claims);
    }
    public String getRoles(String token) {
        return getClaimFromToken(token, claims -> claims.get(ROLES)).toString();
    }
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }
    public boolean isInvalid(String token) {
        return this.isTokenExpired(token);
    }

}