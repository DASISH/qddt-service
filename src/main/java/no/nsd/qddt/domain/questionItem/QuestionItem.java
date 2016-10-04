package no.nsd.qddt.domain.questionItem;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.concept.Concept;
import no.nsd.qddt.domain.controlconstruct.ControlConstruct;
import no.nsd.qddt.domain.question.Question;
import no.nsd.qddt.domain.responsedomain.ResponseDomain;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Question Item is a container for Question (text) and responsedomain
 * This entity introduce a breaking change into the model. it supports early binding of
 * of the containing entities, by also supplying a reference to their revision number.
 * This binding is outside the model which is defined here and used by the framework.
 * This means that when fetching its content it will need to query the revision part of this
 * system, when a revision number is specified.
 *
 * @author Stig Norland
 */

@Audited
@Entity
@Table(name = "QUESTION_ITEM")
public class QuestionItem extends AbstractEntityAudit  {


    @ManyToOne
    @JoinColumn(name = "responsedomain_id")
//    @JsonManagedReference(value = "QuestionItemRef")
    private ResponseDomain responseDomain;

    @Column(name = "responsedomain_revision")
    private long responseDomainRevision;

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "question_id")
    private Question question;

    @Column(name = "question_revision")
    private long questionRevivsion;


//    /// QuestionIntent: what the question is supposed to gather information about.
//    @Column(name = "intent", length = 2000)
//    private String intent;

    @JsonBackReference(value = "conceptRef")
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "questionItems")
    private Set<Concept> concepts = new HashSet<>();

    @OneToMany(mappedBy = "questionItem")
    private Set<ControlConstruct> controlConstructs = new HashSet<>();

    public QuestionItem() {

    }

//    @Override
//    public void setChangeKind(ChangeKind changeKind) {
//        super.setChangeKind(changeKind);
//        getQuestion().setChangeKind(changeKind);
//    }

    @PreRemove
    private void removeReferencesFromQi(){
        getConcepts().forEach( C-> removeFromConcept(C));
        getControlConstructs().forEach(CC-> removeFromControlConstruct(CC));
        setResponseDomain(null);
    }

    private void removeFromControlConstruct(ControlConstruct controlConstruct) {
        controlConstruct.getInstrument().setChangeKind(ChangeKind.UPDATED_HIERARCY_RELATION);
        controlConstruct.getInstrument().getControlConstructs().removeIf(CC->CC.equals(controlConstruct));
        controlConstruct.setInstrument(null);
    }

    public void removeFromConcept(UUID conceptId) {
        concepts.forEach(C->{
            if (C.getId().equals(conceptId)) {
                removeFromConcept(C);
            }
        });
    }

    public void removeFromConcept(Concept concept) {
        System.out.println("QuestionItem->" + getName());

        try {
            concept.getTopicGroup().getStudy().getInstruments().forEach(I -> {
                I.getControlConstructs().removeIf(C -> C.getQuestionItem().equals(this));
            });
        } catch (Exception ex) {
            System.out.println("removeFromConcept (ControlConstructs)->" + ex.getMessage());
        }
        concept.getQuestionItems().removeIf(Qi->Qi.equals(this));
        concepts.remove(concept);
        this.setChangeKind(ChangeKind.UPDATED_HIERARCY_RELATION);
    }

    public ResponseDomain getResponseDomain() {
        return responseDomain;
    }

    public void setResponseDomain(ResponseDomain responseDomain) {
        this.responseDomain = responseDomain;
    }

    public long getResponseDomainRevision() {
        return responseDomainRevision;
    }

    public void setResponseDomainRevision(long responseDomainRevision) {
        this.responseDomainRevision = responseDomainRevision;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public long getQuestionRevivsion() {
        
        return questionRevivsion;
    }

    public void setQuestionRevivsion(long questionRevivsion) {
        this.questionRevivsion = questionRevivsion;
    }

    public Set<Concept> getConcepts() {
        return concepts;
    }

    public void setConcepts(Set<Concept> concepts) {
        this.concepts = concepts;
    }

    public Set<ControlConstruct> getControlConstructs() {
        return controlConstructs;
    }

    public void setControlConstructs(Set<ControlConstruct> controlConstructs) {
        this.controlConstructs = controlConstructs;
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
                ", responseDomain=" +  (responseDomain != null ? responseDomain.getName(): "?")  + '\'' +
                "} " + super.toString();
    }

}


