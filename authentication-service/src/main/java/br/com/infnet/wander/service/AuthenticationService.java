package br.com.infnet.wander.service;

import br.com.infnet.wander.domain.dto.MessageResponse;
import br.com.infnet.wander.domain.exception.UserNotFoundException;
import br.com.infnet.wander.model.ERole;
import br.com.infnet.wander.model.Role;
import br.com.infnet.wander.model.User;
import br.com.infnet.wander.repository.RoleRepository;
import br.com.infnet.wander.repository.UserRepository;
import br.com.infnet.wander.security.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AuthenticationService  {


    private final AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtUtil;

    private final  UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder encoder;

    Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    private static final String PASSWORD_PATTERN =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$";

    private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

    public AuthenticationService(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder encoder) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
    }

    public ResponseEntity<Map<String,String>> getDecryptJWT(String jwt) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {

        String role = jwtUtil.getRolesWithSubstring(jwt);
        String email = jwtUtil.getUsernameFromTokenWithSubstring(jwt);

        Map<String,String> subject = new HashMap<>();
        subject.put("role",role);
        subject.put("email",email);

        return ResponseEntity.ok(subject);
    }
    public static boolean isValid(final String password) {
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public String loginAdmin(String email, String password) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(
                        email,
                        password);
        authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        Optional<User> byEmail = userRepository.findByEmail(email);
        String authority = byEmail.get().getRole().getName().toString();
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        return jwtUtil.generateToken(usernamePasswordAuthenticationToken,authority);
    }

    public ResponseEntity<MessageResponse> signup(String email, String firstname,String lastname, String password, String role) {
        if(!isValid(password)){
            throw new UserNotFoundException("Error: your password is not secure. " +
                    "\n" +
                    "please enter a password with a minimum of 8 characters and a maximum of 20.\n" +
                    "at least one uppercase, lowercase, and special character");
        }

        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new UserNotFoundException(("Error: Email is already in use!"));
        }
        User user = new User(firstname,
                lastname,
                email,
                encoder.encode(password));
        if (role.isBlank()) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            user.setRole(userRole);
        } else {
            switch (role) {
                case "admin":
                    Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    user.setRole(adminRole);
                    break;
                default:
                    Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    user.setRole(userRole);
            }
        }
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
}
