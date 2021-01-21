package no.nsd.qddt.configuration.tbd;


/**
 * @author Stig Norland
 */
//public class SecurityExpressionOperationImpl extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {
//
//
//    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());
//
//    private Object filterObject;
//    private Object returnObject;
//
//    public SecurityExpressionOperationImpl(Authentication authentication) {
//        super(authentication);
//        LOG.info( "SecurityExpressionOperationImpl" );
//    }
//
//    public boolean isMember(Agency agency) {
//        MyUserDetails user = (MyUserDetails)this.getAuthentication().getDetails();
//        return agency == null || user.getAgency().getId().equals( agency.getId() );
//    }
//
//    @Override
//    public Object getFilterObject() {
//        return this.filterObject;
//    }
//
//    @Override
//    public Object getReturnObject() {
//        return this.returnObject;
//    }
//
//    @Override
//    public Object getThis() {
//        return this;
//    }
//
//    @Override
//    public void setFilterObject(Object obj) {
//        this.filterObject = obj;
//    }
//
//    @Override
//    public void setReturnObject(Object obj) {
//        this.returnObject = obj;
//    }
//
//
//}
