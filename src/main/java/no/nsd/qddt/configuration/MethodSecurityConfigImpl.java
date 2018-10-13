package no.nsd.qddt.configuration;

import no.nsd.qddt.security.PermissionEvaluatorImpl;
import no.nsd.qddt.security.SecurityExpressionHandlerImpl;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

/**
 * @author Stig Norland
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MethodSecurityConfigImpl extends GlobalMethodSecurityConfiguration {

    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        System.out.println("createExpressionHandler");

        SecurityExpressionHandlerImpl expressionHandler = new SecurityExpressionHandlerImpl();
        expressionHandler.setPermissionEvaluator(new PermissionEvaluatorImpl());
        return expressionHandler;
    }
}
