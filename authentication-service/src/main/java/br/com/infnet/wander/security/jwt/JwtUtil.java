package br.com.infnet.wander.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {
    
    @Value("${rent-a-car-app.jwt-secret}")
    private String secret;
    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);
    public String generateToken(Authentication authentication) throws IllegalArgumentException, JWTCreationException {
//        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        String name = authentication.getName();
        return JWT.create()
                .withSubject("User Details")
                .withClaim("username",(name))
                .withIssuedAt(new Date())
                .withIssuer("Rent a car Application")
                .withExpiresAt(DateUtils.addHours(new Date(), 4))
                .sign(Algorithm.HMAC256(secret));

    }
    
    public String validateTokenAndRetrieveSubject(String token) {
        try{
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                    .withSubject("User Details")
                    .withIssuer("Rent a car Application").build();

            String subject = "User Details";
            DecodedJWT jwt = verifier.verify(token);
            subject = jwt.getClaim("username").asString();

            return subject;
        } catch (IllegalArgumentException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        }
        return "Not possible to retrieve a user";
    }

    public String generateTokenWithLastName(String lastName) throws IllegalArgumentException, JWTCreationException {

        return JWT.create()
        .withSubject("User Details")
        .withClaim("username", lastName)
        .withIssuedAt(new Date())
        .withIssuer("Rent a car Application")
        .withExpiresAt(DateUtils.addHours(new Date(), 4))
        .sign(Algorithm.HMAC256(secret));
    }

    public String getUserNameFromJwtToken(String token) {
        return JWT.decode(token).getSubject();
    }



}