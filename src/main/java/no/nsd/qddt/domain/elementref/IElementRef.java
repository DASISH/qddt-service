package no.nsd.qddt.domain.elementref;

import no.nsd.qddt.domain.embedded.Version;

import java.util.UUID;

/**
 * @author Stig Norland
 */
public interface IElementRef extends Cloneable {


    ElementKind getElementKind();
    void setElementKind(ElementKind elementKind);

    UUID getRefId();
    void setRefId(UUID refId);

    Integer getRevisionNumber();
    void setRevisionNumber(Integer revisionNumber);

    String getName();
    void setName(String name);

    Version getVersion();
    void setVersion(Version version) ;

    Object getElement();

    void setElement(Object element);
}
