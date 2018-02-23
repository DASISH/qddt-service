package no.nsd.qddt.domain.publication;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
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

    private UUID id;
    private Long revisionNumber;
    private ElementKind elementKind;
    private String name;

    private Integer major;
    private Integer minor;
    private String versionLabel;

    private Object element;


    public PublicationElement() {
    }

    public PublicationElement(ElementKind kind,UUID id,Integer rev) {
        setElementEnum(kind);
        setId(id);
        setRevisionNumber(rev.longValue());
    }


    @Type(type="pg-uuid")
    public UUID getId() {
        return id;
    }
    private void setId(UUID id) {
        this.id = id;
    }



    @Column(name = "revision")
    public Long getRevisionNumber() {
        return revisionNumber;
    }
    public void setRevisionNumber(Long revisionNumber) {
        this.revisionNumber = revisionNumber;
    }


    @Transient
    @JsonSerialize
    @JsonDeserialize
    public String getElementKind() {
        return elementKind.getDescription();
    }
    public void setElementKind(String elementDescription) {
        this.elementKind = ElementKind.getEnum(elementDescription);
    }

    @JsonIgnore
    @Enumerated(EnumType.STRING)
    @Column(name = "element_kind")
    public ElementKind getElementEnum(){
        return elementKind;
    }
    private void setElementEnum(ElementKind elementKind) {
        this.elementKind = elementKind;
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

    @Transient
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
                case QUESTION_ITEM:
                    if (element instanceof QuestionItem)
                        return new QuestionItemJsonView((QuestionItem) element);
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
    }


    @JsonIgnore
    @Transient
    public AbstractEntityAudit getElementAsEntity(){
        return (AbstractEntityAudit)element;
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
