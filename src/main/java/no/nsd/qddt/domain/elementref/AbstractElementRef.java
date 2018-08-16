package no.nsd.qddt.domain.elementref;

/**
 * @author Stig Norland
 */

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import no.nsd.qddt.domain.IElementRefType;
import no.nsd.qddt.domain.embedded.Version;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.UUID;

@Audited
@MappedSuperclass
public abstract class AbstractElementRef implements IElementRef {

    @Enumerated(EnumType.STRING)
    protected ElementKind elementKind;

    @Type(type="pg-uuid")
    protected UUID elementId;

    @Column(name = "element_revision")
    protected Integer elementRevision;

    protected String name;

    protected Integer major;
    protected Integer minor;
    protected String versionLabel;


    @Transient
    @JsonSerialize
    protected Object element;

    public AbstractElementRef() {}

    @JsonCreator
    public AbstractElementRef(@JsonProperty("elementKind")ElementKind kind, @JsonProperty("id")UUID id, @JsonProperty("revisionNumber")Integer rev) {
        setElementKind(kind);
        setElementId(id);
        setElementRevision(rev);
    }

    @Override
    public UUID getElementId() {
        return elementId;
    }

    @Override
    public void setElementId(UUID elementId) {
        this.elementId = elementId;
    }

    @Override
    public Integer getElementRevision() {
        return elementRevision;
    }

    @Override
    public void setElementRevision(Integer elementRevision) {
        this.elementRevision = elementRevision;
    }

    public ElementKind getElementKind(){
        return elementKind;
    }
    public void setElementKind(ElementKind elementKind) {
        this.elementKind = elementKind;
    }


    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Version getVersion() {
        return new Version(major,minor,elementRevision,versionLabel);
    }
    public void setVersion(Version version) {
        major = version.getMajor();
        minor = version.getMinor();
        versionLabel = version.getVersionLabel();
    }


    @JsonSerialize
    public Object getElement() {
        return element;
    }
    public void setElement(Object element) {
        this.element = element;
        if (element instanceof IElementRefType) {
            setName( ((IElementRefType) element).getName() );
            setVersion( ((IElementRefType) element).getVersion() );
            setElementId( ((IElementRefType) element).getId() );
            
            setElementKind(  ElementKind.getEnum( element.getClass().getSimpleName() ) );
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractElementRef)) return false;

        AbstractElementRef that = (AbstractElementRef) o;

        if (elementKind != that.elementKind) return false;
        if (elementId != null ? !elementId.equals( that.elementId ) : that.elementId != null) return false;
        if (elementRevision != null ? !elementRevision.equals( that.elementRevision ) : that.elementRevision != null)  return false;
        return  true;
    }

    @Override
    public int hashCode() {
        int result = elementKind != null ? elementKind.hashCode() : 0;
        result = 31 * result + (elementId != null ? elementId.hashCode() : 0);
        result = 31 * result + (elementRevision != null ? elementRevision.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (major != null ? major.hashCode() : 0);
        result = 31 * result + (minor != null ? minor.hashCode() : 0);
        result = 31 * result + (versionLabel != null ? versionLabel.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "{\"AbstractElementRef\":{"
            + "\"elementKind\":\"" + elementKind + "\""
            + ", \"elementId\":" + elementId
            + ", \"elementRevision\":\"" + elementRevision + "\""
            + ", \"name\":\"" + name + "\""
            + ", \"major\":\"" + major + "\""
            + ", \"minor\":\"" + minor + "\""
            + ", \"versionLabel\":\"" + versionLabel + "\""
            + ", \"item\":\"" + element!=null ? element.toString():"null" + "\""
            + "}}";
    }


}
