package no.nsd.qddt.security.userdetails;

import no.nsd.qddt.domain.user.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author Stig Norland
 */

@Service
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public MyUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

//    @PostConstruct
//    public void completeSetup() {
//        userRepository = applicationContext.getBean( UserRepository.class);
//    }

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        return (UserDetails) userRepository.findByEmailIgnoreCase(name.toLowerCase().trim() ).get();
    }

//    Logger logger = LoggerFactory.getLogger(MyUserDetailsService.class);

//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        logger.info("Loading user details for user {} ...", username);
//
//        // Access some user database here ...
//
//        List<String> allowedUsers = Arrays.asList("basicUser", "staticUser", "olivia.oauth@test.local");
//        if (allowedUsers.contains(username)) {
//            return new MyUserDetails(username, username + "@email.local", new Date(992182578), Arrays.asList("EDITOR", "VIEWER"));
//        } else {
//            throw new UsernameNotFoundException(String.format("Username not found: %s", username));
//        }
//    }

}
