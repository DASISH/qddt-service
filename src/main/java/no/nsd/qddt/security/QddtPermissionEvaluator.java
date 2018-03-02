package no.nsd.qddt.security;

/**
 * @author Stig Norland
 */
//public class QddtPermissionEvaluator implements org.springframework.security.access.PermissionEvaluator {
//
//    @Override
//    public boolean hasPermission(Authentication auth, Object targetDomainObject, Object permission) {
//        if ((auth == null) || (targetDomainObject == null) || !(permission instanceof String)) {
//            return false;
//        }
//        final String targetType = targetDomainObject.getClass().getSimpleName().toUpperCase();
//        return hasPrivilege(auth, targetType, permission.toString().toUpperCase());
//    }
//
//    @Override
//    public boolean hasPermission(Authentication auth, Serializable targetId, String targetType, Object permission) {
//        if ((auth == null) || (targetType == null) || !(permission instanceof String)) {
//            return false;
//        }
//        return hasPrivilege(auth, targetType.toUpperCase(), permission.toString().toUpperCase());
//    }
//
//    private boolean hasPrivilege(Authentication auth, String targetType, String permission) {
//        for (final GrantedAuthority grantedAuth : auth.getAuthorities()) {
//            System.out.println("here " + grantedAuth);
//            if (grantedAuth.getAuthority().startsWith(targetType)) {
//                if (grantedAuth.getAuthority().contains(permission)) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
//
//}