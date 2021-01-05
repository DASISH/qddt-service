package no.nsd.qddt.domain.security;


import no.nsd.qddt.domain.agency.Agency;
import no.nsd.qddt.domain.user.impl.UserDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

/**
 * @author Stig Norland
 */
public class SecurityExpressionOperationImpl extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {


    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private Object filterObject;
    private Object returnObject;

    public SecurityExpressionOperationImpl(Authentication authentication) {
        super(authentication);
        LOG.info( "SecurityExpressionOperationImpl" );
    }

    public boolean isMember(Agency agency) {
        UserDetailsImpl user = (UserDetailsImpl)this.getAuthentication().getDetails();
        return agency == null || user.getUser().getAgency().getId().equals( agency.getId() );
    }

    @Override
    public Object getFilterObject() {
        return this.filterObject;
    }

    @Override
    public Object getReturnObject() {
        return this.returnObject;
    }

    @Override
    public Object getThis() {
        return this;
    }

    @Override
    public void setFilterObject(Object obj) {
        this.filterObject = obj;
    }

    @Override
    public void setReturnObject(Object obj) {
        this.returnObject = obj;
    }


}
