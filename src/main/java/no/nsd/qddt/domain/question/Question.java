package no.nsd.qddt.domain.question;

import no.nsd.qddt.domain.AbstractEntity;
import no.nsd.qddt.utils.StringTool;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

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
public class Question extends AbstractEntity {

    @OrderColumn(name = "children_idx")
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "parent_id")
    private Set<Question> children = new HashSet<>();

    /// Reason why this Question is before or after its siblings
    private String questionIdxRationale;

    /// QuestionIntent: what the question is supposed to gather information about.
    @Column(name = "intent", length = 2000)
    private String intent;

    /// QuestionText: the actual question to ask.
    @Column(name = "question", length = 1500)
    private String question;

    public Question() {

    }

    public Question(String questionText){
        setQuestion(questionText);
    }

    protected Question(Question question) {
        setChildren(question.getChildren().stream().map(q->newCopyOf(q)).collect(Collectors.toSet()));
        setId(question.getId());
        setModifiedBy(question.getModifiedBy());
        setModified(question.getModified());
        setQuestionIdxRationale(question.getQuestionIdxRationale());
        setIntent(question.getIntent());
        setQuestion(question.getQuestion());
    }


    public Set<Question> getChildren() {
        return children;
    }


    public String getQuestionIdxRationale() {
        return questionIdxRationale;
    }

    public void setQuestionIdxRationale(String gridIdxRationale) {
        this.questionIdxRationale = gridIdxRationale;
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

        if (questionIdxRationale != null ? !questionIdxRationale.equals(question1.questionIdxRationale) : question1.questionIdxRationale != null)
            return false;
        if (intent != null ? !intent.equals(question1.intent) : question1.intent != null) return false;
        return !(question != null ? !question.equals(question1.question) : question1.question != null);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (children != null ? children.size() : 0);
        result = 31 * result + (questionIdxRationale != null ? questionIdxRationale.hashCode() : 0);
        result = 31 * result + (intent != null ? intent.hashCode() : 0);
        result = 31 * result + (question != null ? question.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Question{" +
                ", questionIdxRationale='" + questionIdxRationale + '\'' +
                ", intent='" + intent + '\'' +
                ", question='" + question + '\'' +
                "} " + super.toString();
    }

    public Question newCopyOf(){
        return newCopyOf(this);
    }

    private Question newCopyOf(Question question) {
        Question instance = new Question(question);
        instance.setId(null);
        return  instance;
    }
}


