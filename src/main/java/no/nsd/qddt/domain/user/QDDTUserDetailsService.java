package no.nsd.qddt.domain.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Stig Norland
 */

@Service
public class QDDTUserDetailsService implements UserDetailsService {


    private final UserService userService;

    @Autowired
    public QDDTUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        User user = userService.findByName( name );

        List<GrantedAuthority> grantedAuthorities =
            user.getAuthorities().stream().map( (authority) ->
                new SimpleGrantedAuthority( authority.getAuthority() )
            ).collect( Collectors.toList() );

        return new QDDTUserDetails( user, grantedAuthorities );
    }

    public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException {
        User user = userService.findByEmail( email.toLowerCase().trim() );

        List<GrantedAuthority> grantedAuthorities =
                user.getAuthorities().stream().map( (authority) ->
                        new SimpleGrantedAuthority( authority.getAuthority() )
                ).collect( Collectors.toList() );

        return new QDDTUserDetails( user, grantedAuthorities );
    }

}