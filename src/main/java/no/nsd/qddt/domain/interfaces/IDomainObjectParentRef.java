package no.nsd.qddt.domain.interfaces;

/**
 * @author Stig Norland 
 * 
 * A ref is a simple interface which is intended to help
 * reporting backreferences without ending up with a circular reference loop.
 */
 public interface IDomainObjectParentRef extends IDomainObject {

    IParentRef getParentRef();
}
