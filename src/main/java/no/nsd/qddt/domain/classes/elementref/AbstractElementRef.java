package no.nsd.qddt.domain.classes.elementref;

/**
 * @author Stig Norland
 */

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import no.nsd.qddt.domain.controlconstruct.pojo.ConditionConstruct;
import no.nsd.qddt.domain.controlconstruct.pojo.QuestionConstruct;
import no.nsd.qddt.domain.controlconstruct.pojo.StatementItem;
import no.nsd.qddt.domain.classes.interfaces.IElementRef;
import no.nsd.qddt.domain.classes.interfaces.IWebMenuPreview;
import no.nsd.qddt.domain.classes.interfaces.Version;
import no.nsd.qddt.domain.questionitem.QuestionItem;
import no.nsd.qddt.utils.StringTool;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

@MappedSuperclass
public abstract class AbstractElementRef<T extends IWebMenuPreview> implements IElementRef<T> {

    @Enumerated(EnumType.STRING)
    private ElementKind elementKind;

    @Type(type="pg-uuid")
    private UUID elementId;

    @Column(name = "element_revision")
    private Integer elementRevision;

    @Column(name = "element_name", length = 500)
    protected String name;

    @Column(name = "element_major")
    private Integer major;
    @Column(name = "element_minor")
    private Integer minor;
    @Column(name = "element_version_label")
    private String versionLabel;


    @Transient
    @JsonSerialize
    @JsonDeserialize()
    protected T element;

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

    public void setElementId(UUID elementId) {
        this.elementId = elementId;
    }

    @Override
    public Integer getElementRevision() {
        return elementRevision;
    }

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

    @Override
    public T getElement() {
        return element;
    }

    @Override
    public void setElement(T element) {
        this.element = element;
        setValues();
    }

    protected AbstractElementRef<T> setValues() {
        if (getElement() == null) return this;
        if (StringTool.IsNullOrEmpty(getName())) {
            if (element instanceof QuestionItem)
                setName( getElement().getName() + " ➫ " + ((QuestionItem) element).getQuestion() );
            else if (element instanceof StatementItem)
                setName( getElement().getName() + " ➫ " + ((StatementItem) element).getStatement() );
            else if (element instanceof ConditionConstruct) {
                setName( getElement().getName() + " ➫ " + ((ConditionConstruct) element).getCondition() );
                System.out.println( ElementKind.getEnum( element.getClass().getSimpleName() ) + " - ConditionConstruct- name set" );
            } else if (element instanceof QuestionConstruct) {
                System.out.println( ElementKind.getEnum( element.getClass().getSimpleName() ) + " - QuestionConstruct name not set" );
            } else
                System.out.println( ElementKind.getEnum( element.getClass().getSimpleName() ) + " - set name" );
                setName( getElement().getName() );
        }
        setVersion( getElement().getVersion() );
        if (this.elementKind == null)
            setElementKind( ElementKind.getEnum( element.getClass().getSimpleName() ) );
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractElementRef)) return false;

        AbstractElementRef<T> that = (AbstractElementRef) o;

        if (elementKind != that.elementKind) return false;
        if (!Objects.equals( elementId, that.elementId )) return false;
        return Objects.equals( elementRevision, that.elementRevision );
    }

    @Override
    public int hashCode() {
        int result = elementKind != null ? elementKind.hashCode() : 0;
        result = 31 * result + (elementId != null ? elementId.hashCode() : 0);
        result = 31 * result + (elementRevision != null ? elementRevision.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "{" +
            "\"Kind\":" + (getElementKind() == null ? "null" : getElementKind()) + ", " +
            "\"id\":" + (getElementId() == null ? "null" : "\"" + getElementId() + "\"") + ", " +
            "\"version\":" + (getVersion() == null ? "null" : getVersion()) + ", " +
            "\"name\":" + (getName() == null ? "null" : "\"" + getName() + "\"") + ", " +
            "}";
    }

}
