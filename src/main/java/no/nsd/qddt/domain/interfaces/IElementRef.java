package no.nsd.qddt.domain.interfaces;

import no.nsd.qddt.domain.elementref.ElementKind;

import java.util.UUID;

/**
 * @author Stig Norland
 */
public interface IElementRef<T extends IWebMenuPreview> extends Cloneable {

    UUID getElementId();

    Integer getElementRevision();
    void setElementRevision(Integer revisionNumber);

    Version getVersion();

    ElementKind getElementKind();

    String getName();

    T getElement();
    void setElement(T element);

}
