package no.nsd.qddt.domain;

import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.persistence.Table;

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
    //TODO implement

    private Long parentId;

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


    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
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
        if (intent != null ? !intent.equals(question.intent) : question.intent != null) return false;
        if (label != null ? !label.equals(question.label) : question.label != null) return false;
        if (parentId != null ? !parentId.equals(question.parentId) : question.parentId != null) return false;
        if (rankRationale != null ? !rankRationale.equals(question.rankRationale) : question.rankRationale != null)
            return false;
        if (text != null ? !text.equals(question.text) : question.text != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (parentId != null ? parentId.hashCode() : 0);
        result = 31 * result + rank;
        result = 31 * result + (rankRationale != null ? rankRationale.hashCode() : 0);
        result = 31 * result + (intent != null ? intent.hashCode() : 0);
        result = 31 * result + (text != null ? text.hashCode() : 0);
        result = 31 * result + (label != null ? label.hashCode() : 0);
        result = 31 * result + (instruction != null ? instruction.hashCode() : 0);
        return result;
    }


    @Override
    public String toString() {
        return "Question{" +
                "parentId=" + parentId +
                ", rank=" + rank +
                ", rankRationale='" + rankRationale + '\'' +
                ", intent='" + intent + '\'' +
                ", text='" + text + '\'' +
                ", label='" + label + '\'' +
                ", instruction='" + instruction + '\'' +
                '}';
    }
}


