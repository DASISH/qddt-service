package no.nsd.qddt.domain.elementref;

/**
 * @author Stig Norland
 */

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import no.nsd.qddt.domain.IEntityAuditXmlRef;
import no.nsd.qddt.domain.embedded.Version;
import no.nsd.qddt.domain.questionitem.QuestionItem;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Audited
@MappedSuperclass
public abstract class AbstractElementRef implements IElementRef {

    @Enumerated(EnumType.STRING)
    private ElementKind elementKind;

    @Type(type="pg-uuid")
    private UUID elementId;

    @Column(name = "element_revision")
    private Integer elementRevision;

    protected String name;

    private Integer major;
    private Integer minor;
    private String versionLabel;


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
    public IEntityAuditXmlRef getElement() {
        try {
            return (IEntityAuditXmlRef) element;
        } catch (Exception ex ) {
            System.out.println(element.toString());
            return null;
        }
    }
    public void setElement(Object element) {
        this.element = element;
    }


    public AbstractElementRef setValues() {
        if (getElement() instanceof QuestionItem)
            setName( getElement().getName() + " - " + ((QuestionItem) element).getQuestion() );
        else
            setName( getElement().getName());
        setVersion( getElement().getVersion() );
        setElementKind(  ElementKind.getEnum( element.getClass().getSimpleName() ) );
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractElementRef)) return false;

        AbstractElementRef that = (AbstractElementRef) o;

        if (elementKind != that.elementKind) return false;
        if (!Objects.equals( elementId, that.elementId )) return false;
        if (!Objects.equals( elementRevision, that.elementRevision ))  return false;
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
            + ", \"version\":" + getVersion()
            + "}}";
    }


}
