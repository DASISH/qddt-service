package no.nsd.qddt.domain.publication;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.pdf.PdfReport;
import no.nsd.qddt.exception.StackTraceFilter;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Stig Norland
 */
@Entity
@Audited
@Table(name = "PUBLICATION")
public class Publication extends AbstractEntityAudit {

    private String purpose;

    private String status;

    @OrderColumn(name="element_idx")
    @OrderBy("element_idx ASC")
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "PUBLICATION_ELEMENT",joinColumns = @JoinColumn(name="element_id"))
    private List<PublicationElement>  publicationElements = new ArrayList<>();


    public String getPurpose() {
        return purpose;
    }


    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }


    public String getStatus() {
        return status;
    }


    public void setStatus(String status) {
        this.status = status;
    }


    public List<PublicationElement> getPublicationElements() {
        try {
            return publicationElements;
        }catch (Exception ex){
            StackTraceFilter.println(ex.getStackTrace());
            return new ArrayList<>();
        }
    }


    public void setPublicationElements(List<PublicationElement> publicationElements) {
        this.publicationElements = publicationElements;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Publication)) return false;
        if (!super.equals(o)) return false;

        Publication that = (Publication) o;

        return (Objects.equals(status, that.status));

    }


    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (status != null ? status.hashCode() : 0);
        return result;
    }


    @Override
    public String toString() {
        return "Publication{" +
                "purpose='" + purpose + '\'' +
                ", status='" + status + '\'' +
                ", publicationElements=" + publicationElements +
                "} " + super.toString();
    }


    @Override
    public void fillDoc(PdfReport pdfReport) throws IOException {
        for (PublicationElement element:getPublicationElements()){
            element.getElementAsEntity().fillDoc(pdfReport);
        }
    }

}
