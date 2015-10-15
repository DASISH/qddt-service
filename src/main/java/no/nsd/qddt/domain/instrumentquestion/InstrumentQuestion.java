package no.nsd.qddt.domain.instrumentquestion;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.instruction.Instruction;
import no.nsd.qddt.domain.instrument.Instrument;
import no.nsd.qddt.domain.question.Question;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Instrument is the significant relation.
 * Instrument will be asked for all {@link Question} instances it has and the
 * metadata in this class will be used as visual logic for each {@link Question}.
 *
 * @author Stig Norland
 * @author Dag Østgulen Heradstveit
 */
@Audited
@Entity
@Table(name = "INSTRUMENT_QUESTION")
public class InstrumentQuestion extends AbstractEntityAudit {

    @ManyToOne
    @JoinColumn(name = "instrument_id")
    private Instrument instrument;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    @OneToMany(mappedBy="instrumentQuestion", cascade = CascadeType.ALL)
    private Set<Instruction> instructions = new HashSet<>();

    private Long instrumentIdx;

    private String indexRationale;

    private String logic;


    public InstrumentQuestion() {
    }


    public Instrument getInstrument() {
        return instrument;
    }

    public void setInstrument(Instrument instrument) {
        this.instrument = instrument;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Set<Instruction> getInstructions() {
        return instructions;
    }

    public void setInstructions(Set<Instruction> instructions) {
        this.instructions = instructions;
    }

    public Long getInstrumentIdx() {
        return instrumentIdx;
    }

    public void setInstrumentIdx(Long instrumentIdx) {
        this.instrumentIdx = instrumentIdx;
    }

    public String getIndexRationale() {
        return indexRationale;
    }

    public void setIndexRationale(String indexRationale) {
        this.indexRationale = indexRationale;
    }

    public String getLogic() {
        return logic;
    }

    public void setLogic(String logic) {
        this.logic = logic;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InstrumentQuestion)) return false;
        if (!super.equals(o)) return false;

        InstrumentQuestion that = (InstrumentQuestion) o;

        if (instrument != null ? !instrument.equals(that.instrument) : that.instrument != null) return false;
        if (question != null ? !question.equals(that.question) : that.question != null) return false;
        if (instructions != null ? !instructions.equals(that.instructions) : that.instructions != null) return false;
        if (instrumentIdx != null ? !instrumentIdx.equals(that.instrumentIdx) : that.instrumentIdx != null)
            return false;
        if (indexRationale != null ? !indexRationale.equals(that.indexRationale) : that.indexRationale != null)
            return false;
        return !(logic != null ? !logic.equals(that.logic) : that.logic != null);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (instrument != null ? instrument.hashCode() : 0);
        result = 31 * result + (question != null ? question.hashCode() : 0);
        result = 31 * result + (instructions != null ? instructions.hashCode() : 0);
        result = 31 * result + (instrumentIdx != null ? instrumentIdx.hashCode() : 0);
        result = 31 * result + (indexRationale != null ? indexRationale.hashCode() : 0);
        result = 31 * result + (logic != null ? logic.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "InstrumentQuestion{" +
                "instrument=" + instrument +
                ", question=" + question +
                ", instrumentIdx=" + instrumentIdx +
                ", indexRationale='" + indexRationale + '\'' +
                ", logic='" + logic + '\'' +
                '}';
    }
}


