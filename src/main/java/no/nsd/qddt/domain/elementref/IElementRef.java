package no.nsd.qddt.domain.elementref;

import no.nsd.qddt.domain.embedded.Version;

import java.util.UUID;

/**
 * @author Stig Norland
 */
public interface IElementRef<T extends IEntityRef> extends Cloneable {

    ElementKind getElementKind();
    String getName();
    Version getVersion();
    UUID getElementId();

    Integer getElementRevision();
    void setElementRevision(Integer revisionNumber);

    T getElement();
    void setElement(T element);
}
