package no.nsd.qddt.domain;

import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Stig Norland
 * @author Dag Østgulen Heradstveit
 */
@Audited
@Entity
@Table(name = "Survey")
public class Survey extends AbstractEntity{

    @Column(name = "SurveyName")
    private String surveyName;

    @Column(name = "Created")
    private LocalDateTime created;

    @OneToOne
    @ManyToOne
    @JoinColumn(name="user_id")
    private User createBy;

    @OneToMany(mappedBy="survey", cascade = CascadeType.ALL)
    private Set<Comment> comments = new HashSet<>();

    public Survey() {
    }

    public String getSurveyName() {
        return surveyName;
    }

    public void setSurveyName(String surveyName) {
        this.surveyName = surveyName;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public User getCreateBy() {
        return createBy;
    }

    public void setCreateBy(User createBy) {
        this.createBy = createBy;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Survey survey = (Survey) o;

        if (createBy != null ? !createBy.equals(survey.createBy) : survey.createBy != null) return false;
        if (created != null ? !created.equals(survey.created) : survey.created != null) return false;
        if (this.getId() != null ? !this.getId().equals(survey.getId()) : survey.getId()!= null) return false;
        if (surveyName != null ? !surveyName.equals(survey.surveyName) : survey.surveyName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = this.getId() != null ? this.getId().hashCode() : 0;
        result = 31 * result + (surveyName != null ? surveyName.hashCode() : 0);
        result = 31 * result + (created != null ? created.hashCode() : 0);
        result = 31 * result + (createBy != null ? createBy.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Survey{" +
                "id=" + this.getId() +
                ", surveyName='" + surveyName + '\'' +
                ", created=" + created +
                ", createBy='" + createBy + '\'' +
                '}';
    }

}
