package no.nsd.qddt.security;

/**
 * @author Stig Norland
 */
//public class MethodSecurityExpressionHandler extends DefaultMethodSecurityExpressionHandler {
//    private final AuthenticationTrustResolver trustResolver = new AuthenticationTrustResolverImpl();
//
//    @Override
//    protected MethodSecurityExpressionOperations createSecurityExpressionRoot(Authentication authentication, MethodInvocation invocation) {
//        // final CustomMethodSecurityExpressionRoot root = new CustomMethodSecurityExpressionRoot(authentication);
//        final SecurityExpressionRoot root = new SecurityExpressionRoot(authentication);
//        root.setPermissionEvaluator(getPermissionEvaluator());
//        root.setTrustResolver(this.trustResolver);
//        root.setRoleHierarchy(getRoleHierarchy());
//        return root;
//    }
//}