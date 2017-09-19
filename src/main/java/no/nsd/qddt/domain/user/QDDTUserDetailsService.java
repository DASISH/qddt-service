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

import java.util.ArrayList;
import java.util.List;

@Service
public class QDDTUserDetailsService  implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(QDDTUserDetailsService.class);

    private final UserService userService;

    @Autowired
    public QDDTUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        logger.info("Trying to log in " + email);

        User user = userService.findByEmail(email);

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        logger.info("Assigning authorities to " + email);
        user.getAuthorities().forEach((authority) -> {
            grantedAuthorities.add(new SimpleGrantedAuthority(authority.getAuthority()));
            logger.info(authority.getAuthority());
        });

        return new QDDTUserDetails(user, grantedAuthorities);
    }
}