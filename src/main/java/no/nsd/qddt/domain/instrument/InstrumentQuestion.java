package no.nsd.qddt.domain.instrument;

import no.nsd.qddt.domain.AbstractEntityAudit;
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
@Table(name = "instrument_question")
public class InstrumentQuestion extends AbstractEntityAudit {

    @ManyToOne
    @JoinColumn(name = "instrument_id")
    private Instrument instrument;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;


    @ManyToOne
    @JoinColumn(name = "instruction_id")
    private  Instruction instruction;

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
        return !(getInstruction() != null ? !getInstruction().equals(that.getInstruction()) : that.getInstruction() != null);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getInstrument() != null ? getInstrument().hashCode() : 0);
        result = 31 * result + (getQuestion() != null ? getQuestion().hashCode() : 0);
        result = 31 * result + (getInstruction() != null ? getInstruction().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "InstrumentQuestion{" +
                "instrument=" + instrument +
                ", question=" + question +
                '}';
    }
}
