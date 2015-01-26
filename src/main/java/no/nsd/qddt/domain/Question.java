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
 * Question can have
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

    @OneToMany(mappedBy="parent", cascade = CascadeType.ALL)
    private Set<Question> children = new HashSet<>();

    /// Only used for questionText with a parent
    private int rank;

    private String rankRationale;

    /// QuestionIntent: what the questionText is supposed to gather information about.
    private String questionIntent;

    /// QuestionText: the actual questionText to ask.
    @Column(name = "question_text", length = 1500)
    private String questionText;

    /// InstructionLabel: header for instruction
    private String instructionLabel;

    /// Instruction on how to ask questionText.
    @Column(name = "instruction", length = 2000)
    private String instruction;

    @ManyToOne
    @JoinColumn(name = "agency_id")
    private Agency agency;


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

    public Set<Question> getChildren() {return children;}

    /**
     * Add a new comment to the set.
     * @param question to be added to parent.
     */
    public void addChild(Question question) {
        this.children.add(question);
    }

    public void setChildren(Set<Question> children) {this.children = children;}

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

    public String getQuestionIntent() {
        return questionIntent;
    }

    public void setQuestionIntent(String questionIntent) {
        this.questionIntent = questionIntent;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String text) {
        this.questionText = text;
    }

    public String getInstructionLabel() {
        return instructionLabel;
    }

    public void setInstructionLabel(String instructionLabel) {
        this.instructionLabel = instructionLabel;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public Agency getAgency() {return agency;}

    public void setAgency(Agency agency) {this.agency = agency;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Question question = (Question) o;

        if (rank != question.rank) return false;
        if (agency != null ? !agency.equals(question.agency) : question.agency != null) return false;
        if (children != null ? !children.equals(question.children) : question.children != null) return false;
        if (instruction != null ? !instruction.equals(question.instruction) : question.instruction != null)
            return false;
        if (instructionLabel != null ? !instructionLabel.equals(question.instructionLabel) : question.instructionLabel != null)
            return false;
        if (instrumentQuestionSet != null ? !instrumentQuestionSet.equals(question.instrumentQuestionSet) : question.instrumentQuestionSet != null)
            return false;
        if (parent != null ? !parent.equals(question.parent) : question.parent != null) return false;
        if (questionIntent != null ? !questionIntent.equals(question.questionIntent) : question.questionIntent != null)
            return false;
        if (questionText != null ? !questionText.equals(question.questionText) : question.questionText != null)
            return false;
        if (rankRationale != null ? !rankRationale.equals(question.rankRationale) : question.rankRationale != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (parent != null ? parent.hashCode() : 0);
        result = 31 * result + (children != null ? children.hashCode() : 0);
        result = 31 * result + rank;
        result = 31 * result + (rankRationale != null ? rankRationale.hashCode() : 0);
        result = 31 * result + (questionIntent != null ? questionIntent.hashCode() : 0);
        result = 31 * result + (questionText != null ? questionText.hashCode() : 0);
        result = 31 * result + (instructionLabel != null ? instructionLabel.hashCode() : 0);
        result = 31 * result + (instruction != null ? instruction.hashCode() : 0);
        result = 31 * result + (agency != null ? agency.hashCode() : 0);
        result = 31 * result + (instrumentQuestionSet != null ? instrumentQuestionSet.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Question{" +
                "parent=" + parent +
                ", children=" + children +
                ", rank=" + rank +
                ", rankRationale='" + rankRationale + '\'' +
                ", questionIntent='" + questionIntent + '\'' +
                ", questionText='" + questionText + '\'' +
                ", instructionLabel='" + instructionLabel + '\'' +
                ", instruction='" + instruction + '\'' +
                ", agency=" + agency +
                ", instrumentQuestionSet=" + instrumentQuestionSet +
                super.toString() +
                '}';
    }
}


