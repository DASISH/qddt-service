package no.nsd.qddt.domain;

import no.nsd.qddt.domain.instrument.InstrumentQuestion;
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
 * @author Stig Norland
 */

@Audited
@Entity
@Table(name = "question")
public class Question extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name="parent_id")
    private Question parent;

    /// Only used for question with a parent
    private int rank;

    private String rankRationale;

    /// QuestionIntent: what the question is supposed to gather information about.
    private String intent;

    /// QuestionText: the actual question to ask.
    private String text;

    /// InstructionLabel: header for instruction
    private String label;

    /// Instruction on how to ask question.
    private String instruction;

    @OneToMany(mappedBy = "question")
    private Set<InstrumentQuestion> instrumentQuestionSet  = new HashSet<>();

    public Set<InstrumentQuestion> getInstrumentQuestionSet() {
        return instrumentQuestionSet;
    }

    public void setInstrumentQuestionSet(Set<InstrumentQuestion> instrumentQuestionSet) {
        this.instrumentQuestionSet = instrumentQuestionSet;
    }

    public Question getParent() {
        return parent;
    }

    public void setParent(Question parent) {
        this.parent = parent;
    }


    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getRankRationale() {
        return rankRationale;
    }

    public void setRankRationale(String rankRationale) {
        this.rankRationale = rankRationale;
    }

    public String getIntent() {
        return intent;
    }

    public void setIntent(String intent) {
        this.intent = intent;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Question question = (Question) o;

        if (rank != question.rank) return false;
        if (instruction != null ? !instruction.equals(question.instruction) : question.instruction != null)
            return false;
        if (instrumentQuestionSet != null ? !instrumentQuestionSet.equals(question.instrumentQuestionSet) : question.instrumentQuestionSet != null)
            return false;
        if (intent != null ? !intent.equals(question.intent) : question.intent != null) return false;
        if (label != null ? !label.equals(question.label) : question.label != null) return false;
        if (parent != null ? !parent.equals(question.parent) : question.parent != null) return false;
        if (rankRationale != null ? !rankRationale.equals(question.rankRationale) : question.rankRationale != null)
            return false;
        if (text != null ? !text.equals(question.text) : question.text != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (parent != null ? parent.hashCode() : 0);
        result = 31 * result + rank;
        result = 31 * result + (rankRationale != null ? rankRationale.hashCode() : 0);
        result = 31 * result + (intent != null ? intent.hashCode() : 0);
        result = 31 * result + (text != null ? text.hashCode() : 0);
        result = 31 * result + (label != null ? label.hashCode() : 0);
        result = 31 * result + (instruction != null ? instruction.hashCode() : 0);
        result = 31 * result + (instrumentQuestionSet != null ? instrumentQuestionSet.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Question{" +
                "parent=" + parent +
                ", rank=" + rank +
                ", rankRationale='" + rankRationale + '\'' +
                ", intent='" + intent + '\'' +
                ", text='" + text + '\'' +
                ", label='" + label + '\'' +
                ", instruction='" + instruction + '\'' +
                ", instrumentQuestionSet=" + instrumentQuestionSet +
                '}';
    }
}


