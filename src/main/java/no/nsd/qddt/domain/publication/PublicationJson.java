package no.nsd.qddt.domain.publication;

import java.util.ArrayList;
import java.util.List;

import no.nsd.qddt.domain.AbstractJsonEdit;
import no.nsd.qddt.domain.elementref.ElementRef;
import no.nsd.qddt.domain.publicationstatus.PublicationStatus;

/**
 * @author Stig Norland
 */
public class PublicationJson  extends AbstractJsonEdit {

private static final long serialVersionUID = 1L;

private String purpose;

private Long statusId;

private PublicationStatus status;

private List<ElementRef>  publicationElements;

public PublicationJson(Publication publication) {
    super(publication);
    this.purpose = publication.getPurpose();
    this.status = publication.getStatus();
    this.statusId = publication.getStatusId();
    this.publicationElements = publication.getPublicationElements();
}

    public Long getStatusId() {
        return statusId;
    }

    public String getPurpose() {
    return purpose;
}


public PublicationStatus getStatus() {
    return status;
}


public List<ElementRef> getPublicationElements() {
    return publicationElements;
}

}
