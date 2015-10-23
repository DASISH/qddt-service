package no.nsd.qddt.domain.question;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.comment.Comment;
import no.nsd.qddt.domain.commentable.Commentable;
import no.nsd.qddt.domain.concept.Concept;
import no.nsd.qddt.domain.instrumentquestion.InstrumentQuestion;
import no.nsd.qddt.domain.responsedomain.ResponseDomain;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 *
 * QuestionScheme : Contains Question Items, Question Grids, and Question Blocks used by Control Constructs in
 *      creating questionnaires.
 *
 * A Question can have a parent and siblings, if it is part of a ScaleDomain
 *
 * @author Stig Norland
 */

@Audited
@Entity
@Table(name = "QUESTION")
public class Question extends AbstractEntityAudit implements Commentable {

    @ManyToOne
    @JoinColumn(name = "responsedomain_id")
    private ResponseDomain responseDomain;

    @ManyToOne
    @JoinColumn(name="parent_id")
    private Question parent;

    @OneToMany(mappedBy="parent", cascade = CascadeType.ALL)
    private Set<Question> children = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "questions")
    private Set<Concept> concepts = new HashSet<>();

    @OneToMany(mappedBy = "question")
    private Set<InstrumentQuestion> instrumentQuestions = new HashSet<>();

    @Transient
    private Set<Comment> comments = new HashSet<>();

    /// Only used for Questions with a Question parent
    private int gridIdx;

    /// Reason why this Question is before or after its siblings
    private String gridIdxRationale;

    /// QuestionIntent: what the question is supposed to gather information about.
    @Column(name = "intent", length = 2000)
    private String intent;

    /// QuestionText: the actual question to ask.
    @Column(name = "question", length = 1500)
    private String question;

    @Column(name = "concept_reference", nullable = true)
    private UUID conceptReference;

    public Question() {

    }

    public Question getParent() {
        return parent;
    }

    public void setParent(Question parent) {
        this.parent = parent;
    }

    public Set<Question> getChildren() {
        return children;
    }

    public Set<Concept> getConcepts() {
        return concepts;
    }

    public void setConcepts(Set<Concept> concepts) {
        this.concepts = concepts;
    }

    public Set<InstrumentQuestion> getInstrumentQuestions() {
        return instrumentQuestions;
    }

    public void setInstrumentQuestions(Set<InstrumentQuestion> instrumentQuestions) {
        this.instrumentQuestions = instrumentQuestions;
    }

    public ResponseDomain getResponseDomain() {
        return responseDomain;
    }

    public void setResponseDomain(ResponseDomain responseDomain) {
        this.responseDomain = responseDomain;
    }

    public int getGridIdx() {
        return gridIdx;
    }

    public void setGridIdx(int gridIdx) {
        this.gridIdx = gridIdx;
    }

    public String getGridIdxRationale() {
        return gridIdxRationale;
    }

    public void setGridIdxRationale(String gridIdxRationale) {
        this.gridIdxRationale = gridIdxRationale;
    }

    public String getIntent() {
        return intent;
    }

    public void setIntent(String intent) {
        this.intent = intent;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public UUID getConceptReference() {
        return conceptReference;
    }

    public void setConceptReference(UUID conceptReference) {
        this.conceptReference = conceptReference;
    }

    /**
     * Add a new comment to the set.
     * @param question to be added to parent.
     */
    public void addChild(Question question) {
        this.children.add(question);
    }

    public void setChildren(Set<Question> children) {this.children = children;}

    @Override
    public Set<Comment> getComments() {
        return comments;
    }

    @Override
    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    @Override
    public void addComment(Comment comment) {
        comment.setOwnerId(this.getId());
        comments.add(comment);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Question)) return false;
        if (!super.equals(o)) return false;

        Question question1 = (Question) o;

        if (gridIdx != question1.gridIdx) return false;
        if (parent != null ? !parent.equals(question1.parent) : question1.parent != null) return false;
        if (responseDomain != null ? !responseDomain.equals(question1.responseDomain) : question1.responseDomain != null)
            return false;
        if (concepts != null ? !concepts.equals(question1.concepts) : question1.concepts != null) return false;
        if (comments != null ? !comments.equals(question1.comments) : question1.comments != null) return false;
        if (gridIdxRationale != null ? !gridIdxRationale.equals(question1.gridIdxRationale) : question1.gridIdxRationale != null)
            return false;
        if (intent != null ? !intent.equals(question1.intent) : question1.intent != null) return false;
        return !(question != null ? !question.equals(question1.question) : question1.question != null);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (parent != null ? parent.hashCode() : 0);
        result = 31 * result + (responseDomain != null ? responseDomain.hashCode() : 0);
        result = 31 * result + (children != null ? children.hashCode() : 0);
        result = 31 * result + (concepts != null ? concepts.hashCode() : 0);
        result = 31 * result + (comments != null ? comments.hashCode() : 0);
        result = 31 * result + gridIdx;
        result = 31 * result + (gridIdxRationale != null ? gridIdxRationale.hashCode() : 0);
        result = 31 * result + (intent != null ? intent.hashCode() : 0);
        result = 31 * result + (question != null ? question.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Question{" +
                "gridIdx=" + gridIdx +
                ", gridIdxRationale='" + gridIdxRationale + '\'' +
                ", intent='" + intent + '\'' +
                ", question='" + question + '\'' +
                "} " + super.toString();
    }
}


