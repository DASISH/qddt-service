package no.nsd.qddt.domain.parentref;

import no.nsd.qddt.domain.embedded.IUrn;


/**
 * @author Stig Norland 
 * 
 * A ref is a simple interface which is intended to help
 * reporting backreferences without ending up with a circular reference loop.
 */
 public interface IRefs extends IUrn { 
    String getName();
    IRefs getParentRef();
}
