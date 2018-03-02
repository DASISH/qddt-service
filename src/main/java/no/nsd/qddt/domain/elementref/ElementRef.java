package no.nsd.qddt.domain.elementref;

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
public class ElementRef<T> implements Cloneable {

//    @Parent
//    @JsonIgnore
//    private UUID elementId;
//
//    @Column(name = "element_idx" , insertable = false, updatable = false)
//    @JsonIgnore
//    private Integer elementIdx;

    @Enumerated(EnumType.STRING)
    private ElementKind elementKind;

    @Type(type="pg-uuid")
    private UUID id;

    @Column(name = "revision")
    private Long revisionNumber;
    private String name;
    private Integer major;
    private Integer minor;
    private String versionLabel;


    @Transient
    @JsonSerialize
    private Object element;


    public ElementRef(ElementKind kind, UUID id, Long rev) {
        setElementKind(kind);
        setId(id);
        setRevisionNumber(rev);
    }

    public ElementRef(ElementKind kind, UUID id, Integer rev) {
        setElementKind(kind);
        setId(id);
        setRevisionNumber(rev.longValue());
    }

//
//    public UUID getElementId() {
//        return elementId;
//    }
//
//    public void setElementId(UUID elementId) {
//        this.elementId = elementId;
//    }
//
//    public Integer getElementIdx() {
//        return elementIdx;
//    }
//
//    public void setElementIdx(Integer elementIdx) {
//        this.elementIdx = elementIdx;
//    }

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


    public ElementKind getElementKind(){
        return elementKind;
    }

    private void setElementKind(ElementKind elementKind) {
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
        setName( ((AbstractEntityAudit)element).getName() );
        setVersion( ((AbstractEntityAudit)element).getVersion() );
    }


    @JsonIgnore
    @Transient
    public T getElementAs(){
        return (T)element;
    }

    @JsonIgnore
    @Transient
    public AbstractEntityAudit getElementAsEntity(){
        return (AbstractEntityAudit)element;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ElementRef)) return false;

        ElementRef<?> that = (ElementRef<?>) o;

//        if (elementId != null ? !elementId.equals( that.elementId ) : that.elementId != null) return false;
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
    public ElementRef<T> clone() {
        ElementRef retval = new ElementRef<T>(getElementKind(), getId(),getRevisionNumber());
        retval.setVersion( getVersion() );
        retval.setName( getName() );
        return retval;
    }
}
