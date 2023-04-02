package br.com.infnet.wander.security.jwt;

import br.com.infnet.wander.domain.exception.InvalidJwtTokenException;
import br.com.infnet.wander.security.utility.IUserDetailsService;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import javax.servlet.http.HttpServletRequest;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;


import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtils;

    @Autowired
    private IUserDetailsService userDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if ( StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7, headerAuth.length());
        }

        return null;
    }
    private void checkJsonWebToken(String jwt) throws InvalidJwtTokenException {
        if (jwt.isBlank()) {
            throw new InvalidJwtTokenException("Invalid JWT Token in Bearer Header");
        }

    }

    @Override
    protected void doFilterInternal(jakarta.servlet.http.HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = parseJwt((HttpServletRequest) request);
//        checkJsonWebToken(jwt);
        try {
            String username = jwtUtils.validateTokenAndRetrieveSubject(jwt);
            log.info("Extracted username from token: {}", username);

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

//                else {
//                    log.info("Setting security context to AUTHORITY_ORDER");
//                    if (SecurityContextHolder.getContext().getAuthentication() == null) {
//                        SecurityContextHolder.getContext().setAuthentication(
//                                new UsernamePasswordAuthenticationToken(username, "",
//                                        Collections.singletonList(new SimpleGrantedAuthority("AUTHORITY_ORDER"))));
//
//                    }
//                }
        } catch (JWTDecodeException | SignatureVerificationException | TokenExpiredException jwtDecodeException) {
            log.error(jwtDecodeException.getMessage());
            response.setStatus(javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED);
        }


        filterChain.doFilter(request, response);
    }
}
