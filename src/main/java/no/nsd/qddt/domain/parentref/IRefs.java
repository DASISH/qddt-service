package no.nsd.qddt.domain.parentref;

/**
 * @author Stig Norland
 * A ref is a simple interface which is intended to help reporting backreferences
 * without ending up with a circular reference loop.
 */
public interface IRefs extends  IUrnDDI {  //Comparable<IRefs>,

    String getName();

    IRefs getParentRef();

}
