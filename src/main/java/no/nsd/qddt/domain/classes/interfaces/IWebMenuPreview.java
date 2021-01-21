package no.nsd.qddt.domain.classes.interfaces;

import java.util.UUID;

/**
 * @author Stig Norland
 */
public interface IWebMenuPreview {
    UUID getId();
    Version getVersion();
    String getName();
}
