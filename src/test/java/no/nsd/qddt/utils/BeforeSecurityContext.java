package no.nsd.qddt.utils;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.*;

/**
 * This class is used to populate a security context with
 * a valid user when doing inserts into the database in any method
 * annotated by the {@link org.aspectj.lang.annotation.Before} annotation.
 *
 * All inserts need a {@link no.nsd.qddt.domain.user.User} attached as can be seen
 * in {@link no.nsd.qddt.configuration.EntityCreatedModifiedDateAuditEventConfiguration} listener.
 * Created by Dag Østgulen Heradstveit.
 */
public class BeforeSecurityContext {

    private AuthenticationManager authenticationManager;

    public BeforeSecurityContext(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public void createSecurityContext() {
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken("user@example.org", "password");
        Authentication authentication = authenticationManager.authenticate(authRequest);
        org.springframework.security.core.context.SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
    }

    public void destroySecurityContext() {
        org.springframework.security.core.context.SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(null);
    }
}
