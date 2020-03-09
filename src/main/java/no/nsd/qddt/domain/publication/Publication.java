package no.nsd.qddt.domain.publication;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.elementref.ElementRef;
import no.nsd.qddt.domain.pdf.PdfReport;
import no.nsd.qddt.domain.publicationstatus.PublicationStatus;
import no.nsd.qddt.domain.xml.AbstractXmlBuilder;
import no.nsd.qddt.exception.StackTraceFilter;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static org.hibernate.envers.RelationTargetAuditMode.NOT_AUDITED;

/**
 * @author Stig Norland
 */
@Entity
@Audited
@Table(name = "PUBLICATION")
public class Publication extends AbstractEntityAudit {

    private String purpose;

    @Column(name="status_id", nullable = false)
    private Long statusId;

    @ManyToOne(fetch = FetchType.EAGER)
    @Audited(targetAuditMode = NOT_AUDITED)
    @JoinColumn(name="status_id", updatable = false, insertable = false)
    private PublicationStatus status;

    @OrderColumn(name="publication_idx")
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "PUBLICATION_ELEMENT",
        joinColumns = @JoinColumn(name="publication_id", referencedColumnName = "id"))
    private List<ElementRef>  publicationElements = new ArrayList<>();


    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public Long getStatusId() {
        return statusId;
    }

    public void setStatusId(Long statusId) {
        this.statusId = statusId;
    }

    public PublicationStatus getStatus() {
        return status;
    }

    protected void setStatus(PublicationStatus status) {
        this.status = status;
    }

    @Transient
    public boolean isPublished() {
        return this.status.getPublished() == PublicationStatus.Published.EXTERNAL_PUBLICATION;
    }

    public List<ElementRef> getPublicationElements() {
        try {
            return publicationElements;
        }catch (Exception ex){
            StackTraceFilter.println(ex.getStackTrace());
            return new ArrayList<>();
        }
    }

    public void setPublicationElements(List<ElementRef> publicationElements) {
        this.publicationElements = publicationElements;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Publication)) return false;
        if (!super.equals( o )) return false;

        Publication that = (Publication) o;

        if (purpose != null ? !purpose.equals( that.purpose ) : that.purpose != null) return false;
        if (status != null ? !status.equals( that.status ) : that.status != null) return false;
        return publicationElements != null ? publicationElements.equals( that.publicationElements ) : that.publicationElements == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (purpose != null ? purpose.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (publicationElements != null ? publicationElements.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Publication{" +
                "purpose='" + purpose + '\'' +
                ", status='" + status.getLabel() + '\'' +
                ", publicationElements=" + publicationElements +
                "} " + super.toString();
    }

    @Override
    public void fillDoc(PdfReport pdfReport,String counter) {
        pdfReport.addHeader(this,"Publication package");
        pdfReport.addheader2("Purpose");
        pdfReport.addParagraph(getPurpose());
        pdfReport.addheader2("Publication status");
        pdfReport.addParagraph(getStatus().getLabel());
        // pdfReport.addPadding();

        int i=0;
        for (ElementRef element:getPublicationElements()) {
            ((AbstractEntityAudit) element.getElement()).fillDoc(pdfReport,String.valueOf(++i));
        }
    }

    @Override
    protected void beforeUpdate() {}
    @Override
    protected void beforeInsert() {
    }

    @Override
    public AbstractXmlBuilder getXmlBuilder() {
        return null;
	}

}
