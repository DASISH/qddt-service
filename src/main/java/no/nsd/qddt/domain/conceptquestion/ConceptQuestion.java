package no.nsd.qddt.domain.conceptquestion;

import no.nsd.qddt.domain.concept.Concept;
import no.nsd.qddt.domain.question.Question;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * @author Stig Norland
 */
public class ConceptQuestion {

    @ManyToOne
    @JoinColumn(name = "concept_id")
    private Concept concept;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    public Concept getConcept() {
        return concept;
    }

    public void setConcept(Concept concept) {
        this.concept = concept;
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
        if (!(o instanceof ConceptQuestion)) return false;

        ConceptQuestion that = (ConceptQuestion) o;

        if (getConcept() != null ? !getConcept().equals(that.getConcept()) : that.getConcept() != null) return false;
        return !(getQuestion() != null ? !getQuestion().equals(that.getQuestion()) : that.getQuestion() != null);

    }

    @Override
    public int hashCode() {
        int result = getConcept() != null ? getConcept().hashCode() : 0;
        result = 31 * result + (getQuestion() != null ? getQuestion().hashCode() : 0);
        return result;
    }


}
