package no.nsd.qddt.domain.parentref;

import no.nsd.qddt.domain.IEntityUrn;

/**
 * @author Stig Norland
 * A ref is a simple interface which is intended to help reporting backreferences
 * without ending up with a circular reference loop.
 */
interface IRefs<T> extends Comparable<T>, IEntityUrn {

    String getName();

    IRefs<?> getParent();

}
