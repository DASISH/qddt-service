package no.nsd.qddt.domain.question;

import no.nsd.qddt.domain.AbstractEntity;
import no.nsd.qddt.domain.comment.Comment;
import no.nsd.qddt.utils.builders.StringTool;
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
//@FetchProfile(name = "question-with-sub-questions", fetchOverrides = {
//        @FetchProfile.FetchOverride(entity = Question.class, association = "children", mode = FetchMode.JOIN)
//})
@Table(name = "QUESTION")
public class Question extends AbstractEntity {


    @OrderColumn(name = "children_index")
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "parent_id")
    private Set<Question> children = new HashSet<>();


    @Transient
    private Set<Comment> comments = new HashSet<>();


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

    public Question(String questionText){
        setQuestion(questionText);
    }


    public Set<Question> getChildren() {
        return children;
    }


    public String getGridIdxRationale() {
        return gridIdxRationale;
    }

    public void setGridIdxRationale(String gridIdxRationale) {
        this.gridIdxRationale = gridIdxRationale;
    }

    public String getIntent() {
        if (StringTool.IsNullOrEmpty(intent))
            return "";
        else
            return intent;
    }

    public void setIntent(String intent) {
        this.intent = intent;
    }

    public String getQuestion() {
        if (StringTool.IsNullOrEmpty(question))
            return  "";
        else
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Question)) return false;
        if (!super.equals(o)) return false;

        Question question1 = (Question) o;

        if (comments != null ? !comments.equals(question1.comments) : question1.comments != null) return false;
        if (gridIdxRationale != null ? !gridIdxRationale.equals(question1.gridIdxRationale) : question1.gridIdxRationale != null)
            return false;
        if (intent != null ? !intent.equals(question1.intent) : question1.intent != null) return false;
        return !(question != null ? !question.equals(question1.question) : question1.question != null);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (children != null ? children.size() : 0);
        result = 31 * result + (comments != null ? comments.size() : 0);
        result = 31 * result + (gridIdxRationale != null ? gridIdxRationale.hashCode() : 0);
        result = 31 * result + (intent != null ? intent.hashCode() : 0);
        result = 31 * result + (question != null ? question.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Question{" +
                ", gridIdxRationale='" + gridIdxRationale + '\'' +
                ", intent='" + intent + '\'' +
                ", question='" + question + '\'' +
                "} " + super.toString();
    }
}


