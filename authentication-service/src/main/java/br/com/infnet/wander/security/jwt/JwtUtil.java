package br.com.infnet.wander.security.jwt;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.*;
import java.util.function.Function;

@Component
public class JwtUtil {
    
    @Value("${rent-a-car-app.jwt-secret}")
    private String secret;

    public static final String ROLES = "role";
    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }
    public String getRoles(String token) {
        return getClaimFromToken(token, claims -> claims.get(ROLES)).toString();
    }
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public String getUsernameFromTokenWithSubstring(String token) {
        String jwtSubstring = token.substring(7);
        return getClaimFromToken(jwtSubstring, Claims::getSubject);
    }
    public String getRolesWithSubstring(String token) {
        String jwtSubstring = token.substring(7);
        return getClaimFromToken(jwtSubstring, claims -> claims.get(ROLES)).toString();
    }
    public Date getExpirationDateFromTokenWithSubstring(String token) {
        String jwtSubstring = token.substring(7);
        return getClaimFromToken(jwtSubstring, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret.getBytes())
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    //generate token for user
    public String generateToken(Authentication authentication, String authority) throws UnsupportedEncodingException {
        Map<String, Object> claims = new HashMap<>();
        String name = authentication.getName();
        claims.put("username", name);
        claims.put("role", authority);
        return doGenerateToken(claims, name);
    }
    private String doGenerateToken(Map<String, Object> claims, String subject) throws UnsupportedEncodingException {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(DateUtils.addHours(new Date(), 4))
                .signWith(SignatureAlgorithm.HS512, secret.getBytes())
                .compact();
    }
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
//    public String generateToken(Authentication authentication, String authority) throws IllegalArgumentException {
////        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
//        String name = authentication.getName();
////        List<? extends GrantedAuthority> list = authentication.getAuthorities().stream().toList();
////
////        String grantedAuthorityRetrive = list.get(0).toString();
//
//        String serializedSecret = serializeSecret();
//        Claims claims = Jwts.claims().setSubject(name);
//        claims.put("username", name);
//        claims.put("role", authority);
//        return Jwts.builder()
//                .setSubject(("User Details"))
//                .setIssuedAt(new Date())
//                .setClaims(claims)
//                .setExpiration(DateUtils.addHours(new Date(), 4))
//                .signWith(SignatureAlgorithm.HS256, secret)
//                .compact();
//    }
//
//    public Claims validateTokenAndRetrieveSubject(String token) {
//            String serializedSecret = serializeSecret();
//            Claims claims = Jwts.parser()
//                    .setSigningKey(DatatypeConverter.parseBase64Binary(secret))
//                    .requireIssuer("Rent a car Application")
//                    .requireSubject(("User Details"))
//                    .parseClaimsJws(token).getBody();
////            Jws<Claims> jwt = Jwts.parser().setSigningKey(secret).parseClaimsJws("username")
////                    Jwts.parserBuilder()
////                    .setSigningKey(secret)
////                    .build()
////                    .parseClaimsJws(jwt);
////            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(serializedSecret))
////                    .withSubject("User Details")
////                    .withIssuer("Rent a car Application").build();
////
////            String subject = "User Details";
////            DecodedJWT jwt = verifier.verify(token);
//////            subject = jwt.getClaim("username").asString();
////            subject = jwt;
//            return claims;
//
//
//    }

//    public String generateTokenWithLastName(String lastName) throws IllegalArgumentException, JWTCreationException {
//
//        return JWT.create()
//        .withSubject("User Details")
//        .withClaim("username", lastName)
//        .withIssuedAt(new Date())
//        .withIssuer("Rent a car Application")
//        .withExpiresAt(DateUtils.addHours(new Date(), 4))
//        .sign(Algorithm.HMAC256(secret));
//    }

//    public Claims decodeJWT(String token) {
//        String serializedSecret = serializeSecret();
//        Claims claims = Jwts.parser()
//                .setSigningKey(DatatypeConverter.parseBase64Binary(secret))
//                .requireIssuer("Rent a car Application")
//                .requireSubject(("User Details"))
//                .parseClaimsJws(token).getBody();
//
//        return claims;
//        try{
//            String serializedSecret = serializeSecret();
//            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(serializedSecret))
//                    .withSubject("User Details")
//                    .withIssuer("Rent a car Application").build();
//
//            String subject = "User Details";
//
//            DecodedJWT jwt = verifier.verify(token);
//            subject = jwt.getClaim("role").asString();
//
//            return subject;
//        } catch (IllegalArgumentException e) {
//            logger.error("Invalid JWT signature: {}", e.getMessage());
//        }
//        return "Not possible to retrieve a user";
//    }



}