package no.nsd.qddt.domain;

import java.sql.Timestamp;
import java.util.UUID;

/**
 * @author Stig Norland
 *
 * Has ClassKind and are between AbstractEntity and AbstractEntityAudit
 */
public interface IEntityKind {
    UUID getId();
    String getName();
    Timestamp getModified();
    String getClassKind();
}
