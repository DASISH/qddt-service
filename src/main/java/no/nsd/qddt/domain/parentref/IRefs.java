package no.nsd.qddt.domain.parentref;

/**
 * @author Stig Norland
 * A ref is a simple interface which is intended to help reporting backreferences
 * without ending up with a circular reference loop.
 */
interface IRefs<T> extends Comparable<T>, IUrnDDI {

    String getName();

    IRefs<?> getParent();

}
