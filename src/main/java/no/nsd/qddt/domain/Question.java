package no.nsd.qddt.domain;

import no.nsd.qddt.domain.instrument.InstrumentQuestion;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

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
@Table(name = "Question")
public class Question extends AbstractEntityAudit {

    @ManyToOne
    @JoinColumn(name="parent_id")
    private Question parent;

    @OneToMany(mappedBy="parent", cascade = CascadeType.ALL)
    private Set<Question> children = new HashSet<>();

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


    public Question() {

    }

    @OneToMany(mappedBy = "question")
    private Set<InstrumentQuestion> instrumentQuestions = new HashSet<>();

    public Set<InstrumentQuestion> getInstrumentQuestions() {
        return instrumentQuestions;
    }

    public void setInstrumentQuestions(Set<InstrumentQuestion> instrumentQuestions) {
        this.instrumentQuestions = instrumentQuestions;
    }

    public Question getParent() {
        return parent;
    }

    public void setParent(Question parent) {
        this.parent = parent;
    }

    public Set<Question> getChildren() {return children;}

    /**
     * Add a new comment to the set.
     * @param question to be added to parent.
     */
    public void addChild(Question question) {
        this.children.add(question);
    }

    public void setChildren(Set<Question> children) {this.children = children;}

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

    public void setQuestion(String text) {
        this.question = text;
    }

    @OneToMany(cascade =CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name="owner_uuid", foreignKey = @ForeignKey(name="id"))
    private Set<Comment> comments = new HashSet<>();

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public void addComment(Comment comment) {
        comment.setOwnerUUID(this.getId());
        comments.add(comment);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Question question = (Question) o;

        if (gridIdx != question.gridIdx) return false;
        if (children != null ? !children.equals(question.children) : question.children != null) return false;
         if (instrumentQuestions != null ? !instrumentQuestions.equals(question.instrumentQuestions) : question.instrumentQuestions != null)
            return false;
        if (parent != null ? !parent.equals(question.parent) : question.parent != null) return false;
        if (intent != null ? !intent.equals(question.intent) : question.intent != null)
            return false;
        if (this.question != null ? !this.question.equals(question.question) : question.question != null)
            return false;
        if (gridIdxRationale != null ? !gridIdxRationale.equals(question.gridIdxRationale) : question.gridIdxRationale != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (parent != null ? parent.hashCode() : 0);
        result = 31 * result + (children != null ? children.hashCode() : 0);
        result = 31 * result + gridIdx;
        result = 31 * result + (gridIdxRationale != null ? gridIdxRationale.hashCode() : 0);
        result = 31 * result + (intent != null ? intent.hashCode() : 0);
        result = 31 * result + (question != null ? question.hashCode() : 0);
        result = 31 * result + (instrumentQuestions != null ? instrumentQuestions.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Question{" +
                "gridIdx=" + gridIdx +
                ", gridIdxRationale='" + gridIdxRationale + '\'' +
                ", intent='" + intent + '\'' +
                ", question='" + question + '\'' +
                '}';
    }

}


