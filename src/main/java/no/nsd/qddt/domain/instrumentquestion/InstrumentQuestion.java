package no.nsd.qddt.domain.instrumentquestion;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.instrument.Instruction;
import no.nsd.qddt.domain.instrument.Instrument;
import no.nsd.qddt.domain.question.Question;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
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

    @ManyToOne
    @JoinColumn(name = "instruction_id")
    private Instruction instruction;

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

    public Instruction getInstruction() {
        return instruction;
    }

    public void setInstruction(Instruction instruction) {
        this.instruction = instruction;
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

        if (getInstrument() != null ? !getInstrument().equals(that.getInstrument()) : that.getInstrument() != null)
            return false;
        if (getQuestion() != null ? !getQuestion().equals(that.getQuestion()) : that.getQuestion() != null)
            return false;
        if (getInstruction() != null ? !getInstruction().equals(that.getInstruction()) : that.getInstruction() != null)
            return false;
        if (getInstrumentIdx() != null ? !getInstrumentIdx().equals(that.getInstrumentIdx()) : that.getInstrumentIdx() != null)
            return false;
        if (getIndexRationale() != null ? !getIndexRationale().equals(that.getIndexRationale()) : that.getIndexRationale() != null)
            return false;
        return !(getLogic() != null ? !getLogic().equals(that.getLogic()) : that.getLogic() != null);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getInstrument() != null ? getInstrument().hashCode() : 0);
        result = 31 * result + (getQuestion() != null ? getQuestion().hashCode() : 0);
        result = 31 * result + (getInstruction() != null ? getInstruction().hashCode() : 0);
        result = 31 * result + (getInstrumentIdx() != null ? getInstrumentIdx().hashCode() : 0);
        result = 31 * result + (getIndexRationale() != null ? getIndexRationale().hashCode() : 0);
        result = 31 * result + (getLogic() != null ? getLogic().hashCode() : 0);
        return result;
    }


    @Override
    public String toString() {
        return "InstrumentQuestion{" +
                "instrument=" + instrument +
                ", question=" + question +
                ", instruction=" + instruction +
                ", instrumentIdx=" + instrumentIdx +
                ", indexRationale='" + indexRationale + '\'' +
                ", logic='" + logic + '\'' +
                '}';
    }
}


