package no.nsd.qddt.domain.elementref;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.IElementRefType;
import no.nsd.qddt.domain.concept.Concept;
import no.nsd.qddt.domain.concept.json.ConceptJsonEdit;
import no.nsd.qddt.domain.embedded.Version;
import no.nsd.qddt.domain.questionItem.QuestionItem;
import no.nsd.qddt.domain.questionItem.json.QuestionItemJsonView;
import no.nsd.qddt.domain.topicgroup.TopicGroup;
import no.nsd.qddt.domain.topicgroup.json.TopicGroupRevisionJson;
import no.nsd.qddt.exception.StackTraceFilter;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.UUID;


/**
 * @author Stig Norland
 */
@Audited
@Embeddable
public class ElementRef implements Cloneable {


    @Enumerated(EnumType.STRING)
    protected ElementKind elementKind;

    @Type(type="pg-uuid")
    protected UUID id;

    @Column(name = "revision")
    protected Integer revisionNumber;
    protected String name;
    protected Integer major;
    protected Integer minor;
    protected String versionLabel;


    @Transient
    @JsonSerialize
    protected Object element;

    public ElementRef() {}

    @JsonCreator
    public ElementRef(@JsonProperty("elementKind")ElementKind kind, @JsonProperty("id")UUID id, @JsonProperty("revisionNumber")Integer rev) {
        setElementKind(kind);
        setId(id);
        setRevisionNumber(rev);
    }


    public UUID getId() {
        return id;
    }
    private void setId(UUID id) {
        this.id = id;
    }


    public Integer getRevisionNumber() {
        return revisionNumber;
    }
    public void setRevisionNumber(Integer revisionNumber) {
        this.revisionNumber = revisionNumber;
    }


    public ElementKind getElementKind(){
        return elementKind;
    }
    protected void setElementKind(ElementKind elementKind) {
        this.elementKind = elementKind;
    }


    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Version getVersion() {
        return new Version(major,minor,revisionNumber,versionLabel);
    }
    public void setVersion(Version version) {
        major = version.getMajor();
        minor = version.getMinor();
        versionLabel = version.getVersionLabel();
    }


    @JsonSerialize
    public Object getElement() {
        try {
            switch (elementKind) {
                case CONCEPT:
                    if(element instanceof Concept)
                        return new ConceptJsonEdit((Concept) element);
                    break;
                case CONTROL_CONSTRUCT:
                    break;
                case QUESTION_CONSTRUCT:
                    break;
                case STATEMENT_CONSTRUCT:
                    break;
                case SEQUENCE_CONSTRUCT:
                    break;
                case CONDITION_CONSTRUCT:
                    break;
                case INSTRUMENT:
                    break;
                case PUBLICATION:
                    break;
                case QUESTION_ITEM:
                    if (element instanceof QuestionItem)
                        return new QuestionItemJsonView((QuestionItem) element);
                    break;
                case RESPONSEDOMAIN:
                    break;
                case STUDY:
                    break;
                case SURVEY_PROGRAM:
                    break;
                case TOPIC_GROUP:
                    if(element instanceof TopicGroup)
                        return new TopicGroupRevisionJson((TopicGroup) element);
                    break;
            }
        }catch (Exception ex){
            StackTraceFilter.println(ex.getStackTrace());
        }
        return element;
    }
    public void setElement(Object element) {
        this.element = element;
        if (element instanceof IElementRefType ) {
            setName( ((IElementRefType) element).getName() );
            setVersion( ((IElementRefType) element).getVersion() );
            setId( ((IElementRefType) element).getId() );
            setElementKind(  ElementKind.getEnum( element.getClass().getSimpleName() ) );
        }
    }

    @JsonIgnore
    @Transient
    public AbstractEntityAudit getElementAs(){
        if (element instanceof AbstractEntityAudit)
            return (AbstractEntityAudit) element;
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ElementRef)) return false;

        ElementRef that = (ElementRef) o;
        if (elementKind != that.elementKind) return false;
        if (id != null ? !id.equals( that.id ) : that.id != null) return false;
        return revisionNumber != null ? revisionNumber.equals( that.revisionNumber ) : that.revisionNumber == null;
    }

    @Override
    public int hashCode() {
        int result = (elementKind != null ? elementKind.hashCode() : 0);
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (revisionNumber != null ? revisionNumber.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PublicationElement{" +
                "id=" + id +
                ", revisionNumber=" + revisionNumber +
                ", elementKind=" + elementKind +
                ", name='" + name + '\'' +
                ", Version =" + getVersion() + '\'' +
                ", element=" + element +
                '}';
    }

    @Override
    public ElementRef clone() {
        ElementRef retval = new ElementRef(getElementKind(), getId(),getRevisionNumber());
        retval.setVersion( getVersion() );
        retval.setName( getName() );
        return retval;
    }
}
