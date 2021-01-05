package no.nsd.qddt.domain.user.impl;

import no.nsd.qddt.domain.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author Stig Norland
 */

@Service
public class UserDetailsServiceImpl implements UserDetailsService {


    private final UserService userService;

    @Autowired
    public UserDetailsServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        return new UserDetailsImpl(
            userService.findByName(name ));
    }

    public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException {
        return new UserDetailsImpl(
            userService.findByEmail(email.toLowerCase().trim() ));
    }

}
