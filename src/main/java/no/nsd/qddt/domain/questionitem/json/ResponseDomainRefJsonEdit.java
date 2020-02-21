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

    private ResponseDomainRef responseDomainRef;

    public ResponseDomainRefJsonEdit(ResponseDomainRef responseDomainRef) {
        if (responseDomainRef == null) return;
        this.responseDomainRef = responseDomainRef;
    }

    @Override
    public ElementKind getElementKind() {
        return this.responseDomainRef.getElementKind();
    }

    @Override
    public UUID getElementId() {
        return this.responseDomainRef.getElementId();
    }

    @Override
    public Integer getElementRevision() {
        return this.responseDomainRef.getElementRevision();
    }

    @Override
    public void setElementRevision(Integer revisionNumber) {
        this.responseDomainRef.setElementRevision( revisionNumber );
    }

    @Override
    public String getName() {
        return this.responseDomainRef.getName();
    }

    @Override
    public Version getVersion() {
        return this.responseDomainRef.getVersion();
    }

    @Override
    public ResponseDomainJsonEdit getElement() {
        if (this.responseDomainRef.getElement() == null) return null;
        return new ResponseDomainJsonEdit(this.responseDomainRef.getElement());
    }

    @Override
    public void setElement(ResponseDomainJsonEdit element) {

    }
}
