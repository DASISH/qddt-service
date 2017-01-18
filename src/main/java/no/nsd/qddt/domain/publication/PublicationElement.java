package no.nsd.qddt.domain.publication;

import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;

import javax.persistence.Embeddable;
import java.util.UUID;

/**
 * @author Stig Norland
 */
@Audited
@Embeddable
public class PublicationElement  {

    @Type(type="pg-uuid")
    private UUID id;

    private Integer revisionNumber;

    private ElementKind elementKind;


    public PublicationElement() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Integer getRevisionNumber() {
        return revisionNumber;
    }

    public void setRevisionNumber(Integer revisionNumber) {
        this.revisionNumber = revisionNumber;
    }

    public ElementKind getElementKind() {
        return elementKind;
    }

    public void setElementKind(ElementKind elementKind) {
        this.elementKind = elementKind;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PublicationElement)) return false;

        PublicationElement that = (PublicationElement) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (revisionNumber != null ? !revisionNumber.equals(that.revisionNumber) : that.revisionNumber != null)
            return false;
        return elementKind == that.elementKind;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (revisionNumber != null ? revisionNumber.hashCode() : 0);
        result = 31 * result + (elementKind != null ? elementKind.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PublicationElement{" +
                "id=" + id +
                ", revisionNumber=" + revisionNumber +
                ", elementKind=" + elementKind +
                '}';
    }
}
