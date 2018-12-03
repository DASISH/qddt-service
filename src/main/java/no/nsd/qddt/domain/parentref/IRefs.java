package no.nsd.qddt.domain.parentref;

import no.nsd.qddt.domain.embedded.Version;

import java.util.UUID;

/**
 * @author Stig Norland
 * A ref is a simple interface which is intended to help reporting backreferences
 * without ending up with a circular reference loop.
 */
interface IRefs<T> extends Comparable<T> {

    UUID getId();

    String getName();

    Version getVersion();

    String getAgency();

    IRefs getParent();

}
