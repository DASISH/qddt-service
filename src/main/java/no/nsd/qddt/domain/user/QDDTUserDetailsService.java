package no.nsd.qddt.domain.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QDDTUserDetailsService  implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(QDDTUserDetailsService.class);

    private final UserService userService;

    @Autowired
    public QDDTUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        logger.info("Fetching [" + name + "] authorities");
        User user = userService.findByName(name);

        List<GrantedAuthority> grantedAuthorities  =
            user.getAuthorities().stream().map((authority) ->{
                logger.info(authority.getAuthority());
                return new SimpleGrantedAuthority(authority.getAuthority());}
            ).collect(Collectors.toList());

        return new QDDTUserDetails(user, grantedAuthorities);
    }

    public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException {
        logger.info("Fetching [" + email + "] authorities");
        User user = userService.findByEmail(email);

        List<GrantedAuthority> grantedAuthorities  =
            user.getAuthorities().stream().map((authority) ->
            {
                logger.info(authority.getAuthority());
                return new SimpleGrantedAuthority(authority.getAuthority());
            }
        ).collect(Collectors.toList());

        return new QDDTUserDetails(user, grantedAuthorities);
    }

}