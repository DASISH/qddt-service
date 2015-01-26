package no.nsd.qddt.domain.instrument;

import no.nsd.qddt.domain.AbstractEntity;
import no.nsd.qddt.domain.ChangeReason;
import no.nsd.qddt.domain.Question;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Stig Norland
 * @author Dag Østgulen Heradstveit
 */
@Audited
@Entity
@Table(name = "instrument_question")
public class InstrumentQuestion extends AbstractEntity implements Serializable {

    @ManyToOne
    @JoinColumn(name = "instrument_id")
    private Instrument instrument;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        InstrumentQuestion that = (InstrumentQuestion) o;

        if (instrument != null ? !instrument.equals(that.instrument) : that.instrument != null) return false;
        if (question != null ? !question.equals(that.question) : that.question != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (instrument != null ? instrument.hashCode() : 0);
        result = 31 * result + (question != null ? question.hashCode() : 0);
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
