package no.nsd.qddt.domain.elementref;

import no.nsd.qddt.domain.embedded.Version;

import java.util.UUID;

/**
 * @author Stig Norland
 */
public interface IElementRef<T extends IEntityRef> extends Cloneable {


    ElementKind getElementKind();

    UUID getElementId();

    Integer getElementRevision();
    void setElementRevision(Integer revisionNumber);

    String getName();

    Version getVersion();

    T getElement();

     void setElement(T element);
}
