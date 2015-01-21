package no.nsd.qddt.domain.instrument;

import no.nsd.qddt.domain.AbstractEntity;
import no.nsd.qddt.domain.Survey;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author Stig Norland
 */

@Audited
@Entity
@Table(name = "instrument")
public class Instrument extends AbstractEntity{

    @ManyToOne
    @JoinColumn(name="sturvey_id")
    private Survey survey;

    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Instrument that = (Instrument) o;

        if (survey != null ? !survey.equals(that.survey) : that.survey != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (survey != null ? survey.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Instrument{" +
                "survey=" + survey +
                '}';
    }
}
