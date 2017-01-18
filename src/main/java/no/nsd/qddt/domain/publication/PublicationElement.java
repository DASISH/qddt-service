package no.nsd.qddt.domain.publication;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.concept.Concept;
import no.nsd.qddt.domain.controlconstruct.ControlConstruct;
import no.nsd.qddt.domain.instrument.Instrument;
import no.nsd.qddt.domain.questionItem.QuestionItem;
import no.nsd.qddt.domain.study.Study;
import no.nsd.qddt.domain.surveyprogram.SurveyProgram;
import no.nsd.qddt.domain.topicgroup.TopicGroup;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.Embeddable;
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

    private ElementKind elementKind;

    @Transient
    @JsonSerialize
    private String element;


    public PublicationElement() {
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

    public ElementKind getElementKind() {
        return elementKind;
    }

    public void setElementKind(ElementKind elementKind) {
        this.elementKind = elementKind;
    }

    public String getElement() {
        return element;
    }

    public void setElement(Concept element) {
        System.out.println(element);
        this.element = element.toString();
//        Gson gson = new Gson();
//        this.element =  gson.toJson(element);
    }
    public void setElement(ControlConstruct element) {
        System.out.println(element);
        this.element = element.toString();
//        Gson gson = new Gson();
//        this.element =  gson.toJson(element);
    }
    public void setElement(Instrument element) {
        System.out.println(element);
        this.element = element.toString();
//        Gson gson = new Gson();
//        this.element =  gson.toJson(element);
    }
    public void setElement(QuestionItem element) {
        System.out.println(element);
        this.element = element.toString();
//        Gson gson = new Gson();
//        this.element =  gson.toJson(element);
    }
    public void setElement(Study element) {
        System.out.println(element);
        this.element = element.toString();
//        Gson gson = new Gson();
//        this.element =  gson.toJson(element);
    }
    public void setElement(SurveyProgram element) {
        System.out.println(element);
        this.element = element.toString();
//        Gson gson = new Gson();
//        this.element =  gson.toJson(element);
    }
    public void setElement(TopicGroup element) {
        System.out.println(element);
        this.element = element.toString();
//        Gson gson = new Gson();
//        this.element =  gson.toJson(element);
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
