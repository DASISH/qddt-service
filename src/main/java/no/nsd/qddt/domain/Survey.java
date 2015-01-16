package no.nsd.qddt.domain;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Stig Norland
 */
@Entity
@Table(name = "Survey")
public class Survey {

    @Id
    @Column(name = "SurveyId")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long surveyId;

    @Column(name = "SurveyName")
    private String surveyName;

    @Column(name = "Created")
    private LocalDateTime created;

    @Column(name = "CreateBy")
    private String createBy;

    @Column(name = "CommentId")
    private Long commentId;

    private Version version;

    public Survey() {
    }

    public Long getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(Long surveyId) {
        this.surveyId = surveyId;
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

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Survey survey = (Survey) o;

        if (commentId != null ? !commentId.equals(survey.commentId) : survey.commentId != null) return false;
        if (createBy != null ? !createBy.equals(survey.createBy) : survey.createBy != null) return false;
        if (created != null ? !created.equals(survey.created) : survey.created != null) return false;
        if (surveyId != null ? !surveyId.equals(survey.surveyId) : survey.surveyId != null) return false;
        if (surveyName != null ? !surveyName.equals(survey.surveyName) : survey.surveyName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = surveyId != null ? surveyId.hashCode() : 0;
        result = 31 * result + (surveyName != null ? surveyName.hashCode() : 0);
        result = 31 * result + (created != null ? created.hashCode() : 0);
        result = 31 * result + (createBy != null ? createBy.hashCode() : 0);
        result = 31 * result + (commentId != null ? commentId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Survey{" +
                "surveyId=" + surveyId +
                ", surveyName='" + surveyName + '\'' +
                ", created=" + created +
                ", createBy='" + createBy + '\'' +
                ", commentId=" + commentId +
                '}';
    }

}
