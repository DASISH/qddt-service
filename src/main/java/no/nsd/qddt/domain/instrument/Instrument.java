package no.nsd.qddt.domain.instrument;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.Agency;
import no.nsd.qddt.domain.Survey;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Stig Norland
 * @author Dag Østgulen Heradstveit
 */

@Audited
@Entity
@Table(name = "instrument")
public class Instrument extends AbstractEntityAudit {

    @ManyToOne
    @JoinColumn(name="survey_id")
    private Survey survey;

    @OneToMany(mappedBy="instrument", cascade = CascadeType.ALL)
    private Set<InstrumentQuestion> instrumentQuestions = new HashSet<>();

    public Instrument() {
    }

    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }

    public Set<InstrumentQuestion> getInstrumentQuestions() {
        return instrumentQuestions;
    }

    public void setInstrumentQuestions(Set<InstrumentQuestion> instrumentQuestions) {
        this.instrumentQuestions = instrumentQuestions;
    }
    @Override
    /**
     * May cause sideffects if there are users from different agencies on the same module
     * i.e. Agency will change to whomever last saved object.
     */
    public Agency getAgency() {
        return getCreatedBy().getAgency();
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
                super.toString() +
                '}';
    }
}
