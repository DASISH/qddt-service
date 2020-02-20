package no.nsd.qddt.domain.questionitem;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import no.nsd.qddt.domain.elementref.ElementKind;
import no.nsd.qddt.domain.elementref.IElementRef;
import no.nsd.qddt.domain.embedded.Version;
import no.nsd.qddt.domain.responsedomain.ResponseDomain;
import org.hibernate.annotations.Type;
import org.springframework.data.history.Revision;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import java.util.UUID;

/**
 * @author Stig Norland
 */
@Embeddable
public class ResponseDomainRef implements IElementRef<ResponseDomain> {

    @Transient
    @JsonSerialize
    private ResponseDomain element;
    //    @JsonIgnore
    @Type(type="pg-uuid")
    @Column(name="responsedomain_id")
    private UUID elementId;

    @Column(name = "responsedomain_revision")
    private Integer elementRevision;

    @Column(name = "responsedomain_name")
    private String name;

    public ResponseDomainRef() {
    }

    public ResponseDomainRef(ResponseDomain responseDomain ) {
        setElement( responseDomain );
    }
    public ResponseDomainRef(Revision<Integer,ResponseDomain>revision ) {
        setElementRevision( revision.getRevisionNumber() );
        setElement( revision.getEntity() );
    }

    @Override
    @Transient
    public ElementKind getElementKind() {
        return ElementKind.RESPONSEDOMAIN;
    }

    @Override
    public UUID getElementId() {
        return elementId;
    }

    @Override
    public Integer getElementRevision() {
        return elementRevision;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Version getVersion() {
        if (element==null) return null;
        return element.getVersion();
    }

    @Override
    public ResponseDomain getElement() {
        return element;
    }

    public void setElement(ResponseDomain element) {
        this.element = element;

        if (element != null) {
            setElementId( this.getElement().getId() );
            setName( this.element.getName() );
            this.getVersion().setRevision(this.elementRevision);
        } else {
            setName(null);
            setElementRevision( null );
            setElementId( null );
        }
    }


    public void setElementId(UUID elementId) {
        this.elementId = elementId;
    }

    public void setElementRevision(Integer elementRevision) {
        this.elementRevision = elementRevision;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ResponseDomainRef that = (ResponseDomainRef) o;

        if (element != null ? !element.equals( that.element ) : that.element != null) return false;
        if (elementId != null ? !elementId.equals( that.elementId ) : that.elementId != null) return false;
        if (elementRevision != null ? !elementRevision.equals( that.elementRevision ) : that.elementRevision != null)
            return false;
        return name != null ? name.equals( that.name ) : that.name == null;
    }

    @Override
    public int hashCode() {
        int result = element != null ? element.hashCode() : 0;
        result = 31 * result + (elementId != null ? elementId.hashCode() : 0);
        result = 31 * result + (elementRevision != null ? elementRevision.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "{" +
            "\"elementId\":" + (elementId == null ? "null" : elementId) + ", " +
            "\"elementRevision\":" + (elementRevision == null ? "null" : "\"" + elementRevision + "\"") + ", " +
            "\"name\":" + (name == null ? "null" : "\"" + name + "\"") + ", " +
            "\"elementKind\":" + (getElementKind() == null ? "null" : getElementKind()) +
            "}";
    }


}
