package br.com.infnet.wander.controller;

import br.com.infnet.wander.domain.dto.*;
import br.com.infnet.wander.model.ApiError;
import br.com.infnet.wander.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "auth", description = "the auth API")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    

    
    /**
     * POST /auth/login : Login and get a token which is valid for 4h
     *
     * @return Successful logged in (status code 200)
     * or Invalid username/password supplied (status code 400)
     */

    @Operation(operationId = "signin", summary = "Login with email and password", tags = {"auth"}, responses = {
            @ApiResponse(responseCode = "200", description = "Login",
                    content = {@Content(mediaType = "application/json", examples = {@ExampleObject(value = """
                            {
                              "token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJVc2VyIERldGFpbHMiLCJpc3MiOiJDYXIgUmVudGFsIEFwcGxpY2F0aW9uIiwiZXhwIjoxNjQ5MjA2MjY0LCJpYXQiOjE2NDkxOTE4NjQsImVtYWlsIjoiYWRtaW5AYWRtaW4uaW8ifQ.b7jWKmX5eWPTBGB8Bbv5EwD25twMr5oPiGMIZP5XMGo"
                            }
                            """)}, schema = @Schema(implementation = AuthenticationResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid email/password supplied", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))})})
    
    @PostMapping(value = "/auth/signin", produces = {"application/json"}, consumes = {"application/json"})
    public ResponseEntity<AuthenticationResponse> signing(@RequestBody LoginRequest loginRequest) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        return ResponseEntity.ok(new AuthenticationResponse(
                authenticationService.loginAdmin(loginRequest.getEmail(), loginRequest.getPassword())));
    }

    @Operation(operationId = "loginSignUp", summary = "Signup ", tags = {"auth"}, responses = {
            @ApiResponse(responseCode = "200", description = "Signup",
                    content = {@Content(mediaType = "application/json", examples = {@ExampleObject(value = """
                            {
                              "token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJVc2VyIERldGFpbHMiLCJpc3MiOiJDYXIgUmVudGFsIEFwcGxpY2F0aW9uIiwiZXhwIjoxNjQ5MjA2MjY0LCJpYXQiOjE2NDkxOTE4NjQsImVtYWlsIjoiYWRtaW5AYWRtaW4uaW8ifQ.b7jWKmX5eWPTBGB8Bbv5EwD25twMr5oPiGMIZP5XMGo"
                            }
                            """)}, schema = @Schema(implementation = AuthenticationResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))})})

    @PostMapping(value = "/auth/signup", produces = {"application/json"}, consumes = {"application/json"})
    public ResponseEntity<MessageResponse> signUp(@RequestBody SignupRequest signupRequest ) {
        return authenticationService.signup(signupRequest.getEmail(),
                signupRequest.getFirstname(),
                signupRequest.getLastname(),
                signupRequest.getPassword(),
                signupRequest.getRole());
    }

}