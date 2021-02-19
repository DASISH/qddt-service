package no.nsd.qddt.security;

import no.nsd.qddt.domain.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static java.lang.String.format;

@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo
            .findByEmailIgnoreCase( username.trim() )
            .map( mapper -> org.springframework.security.core.userdetails.User
                .withUsername( username )
                .password( mapper.getPassword() )
                .authorities( mapper.getAuthorities() )
                .accountExpired( false )
                .accountLocked( false )
                .credentialsExpired( false )
                .disabled( false )
                .build() )
            .orElseThrow( () -> new UsernameNotFoundException( format( "User with username - %s, not found", username ) ) );
    }



}
