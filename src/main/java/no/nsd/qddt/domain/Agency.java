package no.nsd.qddt.domain;


import no.nsd.qddt.domain.response.ResponseDomain;
import org.hibernate.envers.Audited;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

/**
 * The agency expressed as filed with the DDI Agency ID Registry with optional additional sub-agency extensions.
 * The length restriction of the complete string is done with the means of minLength and maxLength.
 * The regular expression engine of XML Schema has no lookahead possibility.
 *
 * We'll have a relationship with surveys and groups
 *
 * @author Stig Norland
 */
@Audited
@Entity
@Table(name = "agency")
public class Agency extends AbstractEntity {

    @OneToMany(mappedBy="agency", cascade = CascadeType.ALL)
    private Set<Survey> surveys = new HashSet<>();

    @OneToMany(mappedBy="agency", cascade = CascadeType.ALL)
    private Set<ResponseDomain> responses = new HashSet<>();

    @OneToMany(mappedBy="agency", cascade = CascadeType.ALL)
    private Set<Question> questions = new HashSet<>();

    public Set<Survey> getSurveys() {
        return surveys;
    }

    public void setSurveys(Set<Survey> surveys) {
        this.surveys = surveys;
    }

    public Set<ResponseDomain> getResponses() {
        return responses;
    }

    public void setResponses(Set<ResponseDomain> responses) {
        this.responses = responses;
    }

    public Set<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(Set<Question> questions) {
        this.questions = questions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Agency agency = (Agency) o;

        if (questions != null ? !questions.equals(agency.questions) : agency.questions != null) return false;
        if (responses != null ? !responses.equals(agency.responses) : agency.responses != null) return false;
        if (surveys != null ? !surveys.equals(agency.surveys) : agency.surveys != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (surveys != null ? surveys.hashCode() : 0);
        result = 31 * result + (responses != null ? responses.hashCode() : 0);
        result = 31 * result + (questions != null ? questions.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Agency{" +
                "surveys=" + surveys +
                ", responses=" + responses +
                ", questions=" + questions +
                '}';
    }
}
