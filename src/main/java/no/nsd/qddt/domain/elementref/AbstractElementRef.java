package no.nsd.qddt.domain.elementref;

/**
 * @author Stig Norland
 */

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
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
        try {
            switch (elementKind) {
                case CATEGORY:
                    break;
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
        if (elementRevision != null ? !elementRevision.equals( that.elementRevision ) : that.elementRevision != null)
            return false;
        if (name != null ? !name.equals( that.name ) : that.name != null) return false;
        if (major != null ? !major.equals( that.major ) : that.major != null) return false;
        if (minor != null ? !minor.equals( that.minor ) : that.minor != null) return false;
        if (versionLabel != null ? !versionLabel.equals( that.versionLabel ) : that.versionLabel != null) return false;
        return element != null ? element.equals( that.element ) : that.element == null;
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
            + "}}";
    }


}
