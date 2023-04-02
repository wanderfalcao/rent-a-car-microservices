package br.com.infnet.wander.security.utility;

import br.com.infnet.wander.model.ERole;
import br.com.infnet.wander.model.Role;
import br.com.infnet.wander.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@Component
public class IUserDetailsService implements UserDetailsService {
    
    private final PasswordEncoder passwordEncoder;

    @Value("${rent-a-car-app.admin.email:admin@admin.io}")
    String adminUser;
    @Value("${rent-a-car-app.admin.password:admin123}")
    String adminPassword;
    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        if ( ! username.equals(adminUser)) {
//            throw new UsernameNotFoundException("User with specified username could not be found");
//        }
//        return new User(
//                adminUser,
//                passwordEncoder.encode(adminPassword),
//                Collections.singletonList(new SimpleGrantedAuthority("AUTHORITY_ADMINISTRATOR")));

        Optional<br.com.infnet.wander.model.User> byLastnameIgnoreCase = userRepository.findByLastnameIgnoreCase(username);
        Set<Role> roles = new HashSet<>();
        roles.add(new Role(ERole.ROLE_USER));
        return new User(byLastnameIgnoreCase.get().getFirstname() + " " + byLastnameIgnoreCase.get().getLastname(),
                byLastnameIgnoreCase.get().getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(ERole.ROLE_USER.name())));
    }
}
