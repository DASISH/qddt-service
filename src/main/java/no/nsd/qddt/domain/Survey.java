package no.nsd.qddt.domain;

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
@Table(name = "survey")
public class Survey extends AbstractEntity{

    @Column(name = "survey_name")
    private String surveyName;

    @OneToMany(mappedBy="survey", cascade = CascadeType.ALL)
    private Set<Study> studies = new HashSet<>();

    @OneToMany(mappedBy="survey", cascade = CascadeType.ALL)
    private Set<Comment> comments = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "agentcy_id")
    private Agency agency;


    public Survey() {
    }

    public Agency getAgency() {return agency;}

    public void setAgency(Agency agency) {this.agency = agency;}

    public String getSurveyName() {
        return surveyName;
    }

    public void setSurveyName(String surveyName) {
        this.surveyName = surveyName;
    }

    public Set<Study> getStudies() {
        return studies;
    }

    public void setStudies(Set<Study> studies) {
        this.studies = studies;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Survey survey = (Survey) o;

        if (comments != null ? !comments.equals(survey.comments) : survey.comments != null) return false;
        if (this.getCreated() != null ? !this.getCreated().equals(survey.getCreated()) : survey.getCreated() != null) return false;
        if (this.getCreatedBy() != null ? !this.getCreatedBy().equals(survey.getCreatedBy()) : survey.getCreatedBy() != null) return false;
        if (studies != null ? !studies.equals(survey.studies) : survey.studies != null) return false;
        if (surveyName != null ? !surveyName.equals(survey.surveyName) : survey.surveyName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = surveyName != null ? surveyName.hashCode() : 0;
        result = 31 * result + (this.getCreated() != null ? this.getCreated().hashCode() : 0);
        result = 31 * result + (this.getCreatedBy() != null ? this.getCreatedBy().hashCode() : 0);
        result = 31 * result + (studies != null ? studies.hashCode() : 0);
        result = 31 * result + (comments != null ? comments.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Survey{" +
                "surveyName='" + surveyName + '\'' +
                ", created=" + this.getCreated() +
                ", createdBy=" + this.getCreatedBy() +
                '}';
    }
}
