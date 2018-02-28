package no.nsd.qddt.domain.publication;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import no.nsd.qddt.domain.AbstractEntityAudit;
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
public class PublicationElement  {

    @Type(type="pg-uuid")
    private UUID id;

    @Column(name = "revision")
    private Long revisionNumber;

    @Enumerated(EnumType.STRING)
    private ElementKind elementKind;

    private String name;

    @JsonIgnore
    private Integer major;
    @JsonIgnore
    private Integer minor;
    @JsonIgnore
    private
    String versionLabel;

    @Transient
    @JsonSerialize
    private Object element;


    public PublicationElement() {
    }

    public PublicationElement(ElementKind kind,UUID id,Integer rev) {
        setElementEnum(kind);
        setId(id);
        setRevisionNumber(rev.longValue());
    }


    public UUID getId() {
        return id;
    }


    private void setId(UUID id) {
        this.id = id;
    }


    public Long getRevisionNumber() {
        return revisionNumber;
    }


    public void setRevisionNumber(Long revisionNumber) {
        this.revisionNumber = revisionNumber;
    }


    public String getElementKind() {
        return elementKind.getDescription();
    }


    public void setElementKind(String elementDescription) {
        this.elementKind = ElementKind.getEnum(elementDescription);
    }

    private void setElementEnum(ElementKind elementKind) {
        this.elementKind = elementKind;
    }


    @JsonIgnore
    public ElementKind getElementEnum(){
        return elementKind;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    private Version getVersion() {
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

    @JsonIgnore
    @Transient
    public AbstractEntityAudit getElementAsEntity(){
        return (AbstractEntityAudit)element;
    }

    public void setElement(Object element) {
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
                ", name='" + name + '\'' +
                ", Version =" + getVersion() + '\'' +
                ", element=" + element +
                '}';
    }
}
