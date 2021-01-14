package no.nsd.qddt.security;

import no.nsd.qddt.domain.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @author Stig Norland
 */

//@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

//    @PostConstruct
//    public void completeSetup() {
//        userRepository = applicationContext.getBean(UserRepository.class);
//    }

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        return new UserPrincipal(
            userRepository.findByEmailIgnoreCase(name.toLowerCase().trim() ).get()
//            userRepository.findByUsername( name).get()
        );
    }

//    public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException {
//        return new UserPrincipal(
//            userRepository.findByEmailIgnoreCase(email.toLowerCase().trim() ).get()
//        );
//    }

}
