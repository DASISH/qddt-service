package no.nsd.qddt.domain.publication;

import com.itextpdf.layout.Document;
import no.nsd.qddt.domain.AbstractEntityAudit;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Stig Norland
 */
@Entity
@Audited
@Table(name = "PUBLICATION")
public class Publication extends AbstractEntityAudit {

    String purpose;

    String status;

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
            ex.printStackTrace();
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

        return (status == that.status);

    }


    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (status != null ? status.hashCode() : 0);
        return result;
    }

    @Override
    protected void fillDoc(Document document) throws IOException {

    }
}
