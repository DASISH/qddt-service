package no.nsd.qddt.domain.questionItem;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.concept.Concept;
import no.nsd.qddt.domain.controlconstruct.ControlConstruct;
import no.nsd.qddt.domain.question.Question;
import no.nsd.qddt.domain.responsedomain.ResponseDomain;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * QuestionScheme : Contains Question Items
 *
 * @author Stig Norland
 */

@Audited
@Entity
//@FetchProfile(name = "question-with-sub-questions", fetchOverrides = {
//        @FetchProfile.FetchOverride(entity = Question.class, association = "children", mode = FetchMode.JOIN)
//})
@Table(name = "QUESTION_ITEM")
public class QuestionItem extends AbstractEntityAudit  {

    @ManyToOne
    @JoinColumn(name = "responsedomain_id")
    private ResponseDomain responseDomain;

//    @Embedded
    private long responseDomainRevision;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

//    @Embedded
    private long questionRevivsion;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "questionItems")
    private Set<Concept> concepts = new HashSet<>();

    @OneToMany(mappedBy = "questionItem")
    private Set<ControlConstruct> controlConstructs = new HashSet<>();

    public QuestionItem() {

    }

    public Set<Concept> getConcepts() {
        return concepts;
    }

    public void setConcepts(Set<Concept> concepts) {
        this.concepts = concepts;
    }

    public void addConcept(Concept concept){
        if (!this.concepts.contains(concept)){
            this.concepts.add(concept);
            this.setChangeKind(ChangeKind.UPDATED_HIERARCY_RELATION);
       }
    }

    public long getQuestionRevivsion() {
        return questionRevivsion;
    }

    public void setQuestionRevivsion(long questionRevivsion) {
        this.questionRevivsion = questionRevivsion;
    }

    public long getResponseDomainRevision() {
        return responseDomainRevision;
    }

    public void setResponseDomainRevision(long responseDomainRevision) {
        this.responseDomainRevision = responseDomainRevision;
    }

    public Set<ControlConstruct> getControlConstructs() {
        return controlConstructs;
    }

    public void setControlConstructs(Set<ControlConstruct> controlConstructs) {
        this.controlConstructs = controlConstructs;
    }

    public ResponseDomain getResponseDomain() {
        return responseDomain;
    }

    public void setResponseDomain(ResponseDomain responseDomain) {
        this.responseDomain = responseDomain;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof QuestionItem)) return false;
        if (!super.equals(o)) return false;

        QuestionItem questionItem = (QuestionItem) o;

        if (responseDomain != null ? !responseDomain.equals(questionItem.responseDomain) : questionItem.responseDomain != null)
            return false;
        return !(question != null ? !question.equals(questionItem.question) : questionItem.question != null);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (responseDomain != null ? responseDomain.hashCode() : 0);
        result = 31 * result + (question != null ? question.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "QuestionItem{" +
                ", question='" + question.getQuestion() + '\'' +
                ", responseDomain=" + responseDomain.getName()  + '\'' +
                "} " + super.toString();
    }
}


