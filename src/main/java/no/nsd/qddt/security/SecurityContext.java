package no.nsd.qddt.security;

import no.nsd.qddt.security.user.QDDTUserDetails;
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
    public static QDDTUserDetails getUserDetails() {
        return (QDDTUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
