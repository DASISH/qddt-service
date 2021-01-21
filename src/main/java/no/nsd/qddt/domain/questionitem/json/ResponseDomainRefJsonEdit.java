package no.nsd.qddt.domain.questionitem.json;

import no.nsd.qddt.domain.classes.elementref.ElementKind;
import no.nsd.qddt.domain.classes.elementref.ElementRefResponseDomain;
import no.nsd.qddt.domain.classes.interfaces.IElementRef;
import no.nsd.qddt.domain.classes.interfaces.Version;
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


    public ResponseDomainRefJsonEdit(ElementRefResponseDomain elementRefResponseDomain) {
        if (elementRefResponseDomain == null) return;
        elementKind = elementRefResponseDomain.getElementKind();
        elementId = elementRefResponseDomain.getElementId();
        elementRevision = elementRefResponseDomain.getElementRevision();
        if (elementRefResponseDomain.getElement() != null)
            element =  new ResponseDomainJsonEdit( elementRefResponseDomain.getElement());
        name = elementRefResponseDomain.getName();
        version = elementRefResponseDomain.getVersion();
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
