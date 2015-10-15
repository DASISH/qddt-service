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

    private Long instrumentIndex;

    private String indexRationale;

    private String logic;

    private String instruction;


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

    public Long getInstrumentIndex() {
        return instrumentIndex;
    }

    public void setInstrumentIndex(Long instrumentIndex) {
        this.instrumentIndex = instrumentIndex;
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
        if (getInstrumentIndex() != null ? !getInstrumentIndex().equals(that.getInstrumentIndex()) : that.getInstrumentIndex() != null)
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
        result = 31 * result + (getInstrumentIndex() != null ? getInstrumentIndex().hashCode() : 0);
        result = 31 * result + (getIndexRationale() != null ? getIndexRationale().hashCode() : 0);
        result = 31 * result + (getLogic() != null ? getLogic().hashCode() : 0);
        return result;
    }


    @Override
    public String toString() {
        return "InstrumentQuestion{" +
                "instrument=" + instrument +
                ", question=" + question +
                ", instrumentIndex=" + instrumentIndex +
                ", indexRationale='" + indexRationale + '\'' +
                ", logic='" + logic + '\'' +
                '}';
    }
}


