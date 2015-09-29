package assets;

import no.nsd.qddt.domain.user.QDDTUserDetailsService;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

/**
 * This method exists while we wait for Spring 1.3 which will support TestExecutionListener
 * for classes we need to use the annotated user classes. Currently the stack must move to
 * a regular Spring application without the magic of Boot to support this due to limitations
 * in {@link org.springframework.boot.test.IntegrationTest}
 *
 * @author Dag Østgulen Heradstveit
 */
public class HttpMockAuthSession {

    public static MockHttpSession createSession(QDDTUserDetailsService qddtUserDetailsService, String username) {
        UserDetails principal = qddtUserDetailsService.loadUserByUsername(username);
        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, principal.getPassword(), principal.getAuthorities());
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);

        MockHttpSession session = new MockHttpSession();
        session.setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                securityContext);

        return session;
    }

}
