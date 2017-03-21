package no.nsd.qddt.domain.publication;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import no.nsd.qddt.domain.AbstractEntity;
import no.nsd.qddt.domain.AbstractEntityAudit;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Transient;
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


    @Enumerated(EnumType.STRING)
    private ElementKind elementKind;


    @Transient
    @JsonSerialize
    private Object element;


    public PublicationElement() {
    }

    public PublicationElement(ElementKind kind,UUID id,Integer rev) {
        setElementEnum(kind);
        setId(id);
        setRevisionNumber(rev);
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


    public String getElementKind() {
        return elementKind.getDescription();
    }


    public void setElementKind(String elementDescription) {
        this.elementKind = ElementKind.getEnum(elementDescription);
    }


    public void setElementEnum(ElementKind elementKind) {
        this.elementKind = elementKind;
    }


    @JsonIgnore
    public ElementKind getElementEnum(){
        return elementKind;
    }


    public Object getElement() {
        return element;
    }

    public void setElement(Object element) {
        this.element = element;
    }

    public void setElement(AbstractEntityAudit element) {
        this.element = element;
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
