package no.nsd.qddt.configuration;

import no.nsd.qddt.domain.user.User;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/**
 * @author Stig Norland
 */
public class AuditAwareImpl implements AuditorAware<User> {
    @Override
    public Optional<User> getCurrentAuditor() {
        return Optional.ofNullable( SecurityContextHolder.getContext())
            .map( SecurityContext::getAuthentication)
            .filter( Authentication::isAuthenticated)
            .map(Authentication::getPrincipal)
            .map( User.class::cast);
//            .map(User::);
    }
}
