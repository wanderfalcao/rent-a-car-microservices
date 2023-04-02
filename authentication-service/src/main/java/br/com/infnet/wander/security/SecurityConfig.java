package br.com.infnet.wander.security;

import br.com.infnet.wander.security.jwt.AuthEntryPointJwt;
import br.com.infnet.wander.security.utility.IUserDetailsService;
import br.com.infnet.wander.security.jwt.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableGlobalMethodSecurity(
        // securedEnabled = true,
        // jsr250Enabled = true,
        prePostEnabled = true)
public class SecurityConfig  {


    @Autowired
    IUserDetailsService userDetailsService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;
    Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    @Bean
    public JwtFilter authenticationJwtTokenFilter() {
        return new JwtFilter();
    }
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("*").allowedMethods("*");
            }
        };
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests().antMatchers("/api/v1/**").permitAll()
                .anyRequest().authenticated();

        http.authenticationProvider(authenticationProvider());

//        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//
//        http.logout(logout -> logout
//                        .logoutUrl("/api/v1/auth/logout")
//                        .addLogoutHandler(new SecurityContextLogoutHandler())
//                )
//                .formLogin()
//                .disable()
//                .csrf()
//                .disable()
//                .cors()
//                .disable()
//                .authorizeHttpRequests()
//                .antMatchers("/**").permitAll()
//                .anyRequest().authenticated()
//                .and()
//                .userDetailsService(userDetailsService)
//                .exceptionHandling()
//                .authenticationEntryPoint(
//                        (request, response, authException) -> {
//                            logger.error("An exception occurred at authenticationEntryPoint: {}",
//                                    authException.toString());
//                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
//                        }
//                )
//                .and()
//                .sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
//    }

//    protected void configure(HttpSecurity http) throws Exception {
//
//        http.logout(logout -> logout
//                        .logoutUrl("/api/v1/auth/logout")
//                        .addLogoutHandler(new SecurityContextLogoutHandler())
//                )
//                .formLogin()
//                .disable()
//                .csrf()
//                .disable()
//                .cors()
//                .disable()
//                .authorizeHttpRequests()
//                .antMatchers("/**").permitAll()
//                .anyRequest().authenticated()
//                .and()
//                .userDetailsService(userDetailsService)
//                .exceptionHandling()
//                .authenticationEntryPoint(
//                        (request, response, authException) -> {
//                            logger.error("An exception occurred at authenticationEntryPoint: {}",
//                                    authException.toString());
//                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
//                        }
//                )
//                .and()
//                .sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                .addFilterBefore(filter, EmailPassword);
//    }

}