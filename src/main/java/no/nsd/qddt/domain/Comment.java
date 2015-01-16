package no.nsd.qddt.domain;

import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Dag Østgulen Heradstveit
 */
@Audited
@Entity
@Table(name = "comment")
public class Comment extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name="parent_id")
    private Comment parent;

    @OneToMany(mappedBy="parent", cascade = CascadeType.ALL)
    private Set<Comment> childen = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "survey_id", insertable = false, updatable = false, unique = true)
    private Survey survey;

    @Column(name = "comment")
    public String comment;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User createdBy;

    @Column(name = "created")
    private boolean created;

    public Comment() {
    }

    public Comment getParent() {
        return parent;
    }

    public void setParent(Comment parent) {
        this.parent = parent;
    }

    public Set<Comment> getChilden() {
        return childen;
    }

    public void setChilden(Set<Comment> childen) {
        this.childen = childen;
    }

    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public boolean isCreated() {
        return created;
    }

    public void setCreated(boolean created) {
        this.created = created;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Comment comment1 = (Comment) o;

        if (created != comment1.created) return false;
        if (childen != null ? !childen.equals(comment1.childen) : comment1.childen != null) return false;
        if (comment != null ? !comment.equals(comment1.comment) : comment1.comment != null) return false;
        if (createdBy != null ? !createdBy.equals(comment1.createdBy) : comment1.createdBy != null) return false;
        if (parent != null ? !parent.equals(comment1.parent) : comment1.parent != null) return false;
        if (survey != null ? !survey.equals(comment1.survey) : comment1.survey != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = parent != null ? parent.hashCode() : 0;
        result = 31 * result + (childen != null ? childen.hashCode() : 0);
        result = 31 * result + (survey != null ? survey.hashCode() : 0);
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        result = 31 * result + (createdBy != null ? createdBy.hashCode() : 0);
        result = 31 * result + (created ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "parent=" + parent +
                ", survey=" + survey +
                ", comment='" + comment + '\'' +
                ", createdBy=" + createdBy +
                ", created=" + created +
                '}';
    }
}
