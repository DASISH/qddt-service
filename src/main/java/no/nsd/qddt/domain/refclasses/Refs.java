package no.nsd.qddt.domain.refclasses;

import java.util.UUID;

/**
 * @author Stig Norland
 * A ref is a simple interface which is intended to help reporting backreferences
 * without ending up with a circular reference loop.
 */
public interface Refs<T> extends Comparable<T> {
    String getName();

    void setName(String name);

    UUID getId();

    void setId(UUID id);

}
