package br.com.infnet.wander.security.utility;


import br.com.infnet.wander.model.User;
import br.com.infnet.wander.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


@Service
public class IUserDetailsService implements UserDetailsService {



    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with email:"+email+" could not be found"));
        UserDetailsImpl x = UserDetailsImpl.build(user);
        return x;

    }
}
