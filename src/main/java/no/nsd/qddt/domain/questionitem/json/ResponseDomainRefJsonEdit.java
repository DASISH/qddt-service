package no.nsd.qddt.domain.questionitem.json;

import no.nsd.qddt.domain.elementref.ElementKind;
import no.nsd.qddt.domain.elementref.IElementRef;
import no.nsd.qddt.domain.embedded.Version;
import no.nsd.qddt.domain.embedded.ResponseDomainRef;
import no.nsd.qddt.domain.responsedomain.json.ResponseDomainJsonEdit;

import java.util.UUID;

/**
 * @author Stig Norland
 */
public class ResponseDomainRefJsonEdit implements IElementRef<ResponseDomainJsonEdit> {

    private ElementKind elementKind;
    private UUID elementId;
    private Integer elementRevision;
    private ResponseDomainJsonEdit element;
    private String name;
    private Version version;


    public ResponseDomainRefJsonEdit(ResponseDomainRef responseDomainRef) {
        if (responseDomainRef == null) return;
        elementKind = responseDomainRef.getElementKind();
        elementId = responseDomainRef.getElementId();
        elementRevision = responseDomainRef.getElementRevision();
        if (responseDomainRef.getElement() != null)
            element =  new ResponseDomainJsonEdit(responseDomainRef.getElement());
        name = responseDomainRef.getName();
        version = responseDomainRef.getVersion();
    }

    @Override
    public ElementKind getElementKind() {
        return this.elementKind;
    }

    @Override
    public UUID getElementId() {
        return this.elementId;
    }

    @Override
    public Integer getElementRevision() {
        return this.elementRevision;
    }

    @Override
    public void setElementRevision(Integer revisionNumber) {
        this.elementRevision = revisionNumber;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Version getVersion() {
        return this.version;
    }

    @Override
    public ResponseDomainJsonEdit getElement() {
        return this.element;
    }

    @Override
    public void setElement(ResponseDomainJsonEdit element) {
        this.element = element;
    }

}
