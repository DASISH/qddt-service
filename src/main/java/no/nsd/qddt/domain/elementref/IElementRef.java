package no.nsd.qddt.domain.elementref;

import no.nsd.qddt.domain.IElementRefType;
import no.nsd.qddt.domain.IEntityAuditXmlRef;
import no.nsd.qddt.domain.embedded.Version;

import java.util.UUID;

/**
 * @author Stig Norland
 */
public interface IElementRef<T extends IElementRefType> extends Cloneable {


    ElementKind getElementKind();
//    void setElementKind(ElementKind elementKind);

    UUID getElementId();
//    void setElementId(UUID refId);

    Integer getElementRevision();
    void setElementRevision(Integer revisionNumber);

    String getName();
//    void setName(String name);

    Version getVersion();
//    void setVersion(Version version) ;

    T getElement();

     void setElement(T element);
}
