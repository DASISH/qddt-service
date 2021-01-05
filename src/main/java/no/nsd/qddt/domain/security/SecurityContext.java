package no.nsd.qddt.domain.security;

import no.nsd.qddt.domain.user.impl.UserDetailsImpl;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Helper class providing quick access to security context data.
 *
 * @author Dag Østgulen Heradstveit
 */
public class SecurityContext {

    /**
     * Return the current {@link org.springframework.security.core.userdetails.UserDetails}
     * @return user details
     */
    public static UserDetailsImpl getUserDetails() {
        return (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
