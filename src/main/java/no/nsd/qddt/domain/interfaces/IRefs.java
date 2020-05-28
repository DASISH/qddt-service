package no.nsd.qddt.domain.interfaces;

import no.nsd.qddt.domain.agency.Agency;


/**
 * @author Stig Norland 
 * 
 * A ref is a simple interface which is intended to help
 * reporting backreferences without ending up with a circular reference loop.
 */
 public interface IRefs extends IEntityRef {
//    UUID getId();
//    Version getVersion();
//    String getName();
    Agency getAgency();
    IParentRef getParentRef();
}
