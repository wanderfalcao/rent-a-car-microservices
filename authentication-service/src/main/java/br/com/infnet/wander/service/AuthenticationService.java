package br.com.infnet.wander.service;

import br.com.infnet.wander.domain.dto.MessageResponse;
import br.com.infnet.wander.domain.exception.OrderNotFoundException;
import br.com.infnet.wander.domain.exception.UserNotFoundException;
import br.com.infnet.wander.model.ERole;
import br.com.infnet.wander.model.Order;
import br.com.infnet.wander.model.Role;
import br.com.infnet.wander.model.User;
import br.com.infnet.wander.repository.OrderRepository;
import br.com.infnet.wander.repository.RoleRepository;
import br.com.infnet.wander.repository.UserRepository;
import br.com.infnet.wander.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Service
public class AuthenticationService {

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder encoder;

    Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    private static final String PASSWORD_PATTERN =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$";

    private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

    public static boolean isValid(final String password) {
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public String loginAdmin(String username, String password) {

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(
                        username,
                        password);
        authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        return jwtUtil.generateToken(usernamePasswordAuthenticationToken);
    }
    
    public String loginOrder(BigInteger orderId, String lastName) throws OrderNotFoundException {
        logger.info("Logging user in using orderId and lastName");
        Order order = orderRepository.findByOrderIdAndLastNameAllIgnoreCase(orderId, lastName).orElseThrow(
                () -> {
                    throw new OrderNotFoundException(String.format("Order with id %s was not found", orderId.toString()));
                }
        );
        return jwtUtil.generateTokenWithLastName(order.getLastName());
    }

    public ResponseEntity<MessageResponse> signup(String email, String firstname,String lastname, String password, Set<String> role) {
        Set<Role> roles = new HashSet<>();
        if(!isValid(password)){
            throw new UserNotFoundException("Error: your password is not secure. " +
                    "\n" +
                    "please enter a password with a minimum of 8 characters and a maximum of 20.\n" +
                    "at least one uppercase, lowercase, and special character");
        }
        if (userRepository.existsByFirstnameIgnoreCase(firstname)) {
            throw new UserNotFoundException("Error: first name is already taken!");
        }
        if (userRepository.existsByLastnameIgnoreCase(lastname)) {
            throw new UserNotFoundException("Error: last name is already taken!");
        }
        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new UserNotFoundException(("Error: Email is already in use!"));
        }
        User user = new User(firstname,
                lastname,
                email,
                encoder.encode(password));
        if (role == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            for (String eachRole : role) {
                switch (eachRole) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            }
        }
        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
}