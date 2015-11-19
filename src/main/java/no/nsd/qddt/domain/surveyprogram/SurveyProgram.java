package no.nsd.qddt.domain.surveyprogram;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.comment.Comment;
import no.nsd.qddt.domain.commentable.Commentable;
import no.nsd.qddt.domain.study.Study;
import no.nsd.qddt.domain.user.User;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * <ul class="inheritance">
 * <li>A Survey is a root element of this model. Every Survey has atleast one Study and one Instrument.
 *     <ul class="inheritance">
 *         <li>A Study will have of one or more Modules.</li>
 *         <ul class="inheritance">
 *             <li>A Module will have one or more Concepts.</li>
 *             <ul class="inheritance">
 *                 <li>A Concept consist of one or more Questions.</li>
 *                 <ul class="inheritance">
 *                     <li>Every Question will have a ResponseDomain.</li>
 *                 </ul>
 *              </ul>
 *          </ul>
 *      </ul>
 *      <ul class="inheritance"><li>An Instrument will have a ordered list of Questions, all of which are contained in Concepts
 *      belonging to Modules that belongs to the Studies that this Survey has.</li>
 *      </ul>
 * </li>
 * </ul>
 * @author Stig Norland
 * @author Dag Østgulen Heradstveit
 */

@Audited
@Entity
@Table(name = "SURVEY_PROGRAM")
public class SurveyProgram extends AbstractEntityAudit implements Commentable {

    @OneToMany(mappedBy = "surveyProgram",fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Study> studies = new HashSet<>();

    private String description;

    @OneToMany
    @JoinColumn(name="author_id")
    private Set<User> authors = new HashSet<>();


    @Transient
    private Set<Comment> comments = new HashSet<>();

    public SurveyProgram() {

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<User> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<User> authors) {
        this.authors = authors;
    }

    public Set<Study> getStudies() {
        return studies;
    }

    public void setStudies(Set<Study> studies) {
        this.studies = studies;
    }

    @Override
    public Set<Comment> getComments() {
        return comments;
    }

    @Override
    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    @Override
    public void addComment(Comment comment) {
        comment.setOwnerId(this.getId());
        comments.add(comment);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SurveyProgram)) return false;
        if (!super.equals(o)) return false;

        SurveyProgram that = (SurveyProgram) o;

        if (studies != null ? !studies.equals(that.studies) : that.studies != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (authors != null ? !authors.equals(that.authors) : that.authors != null) return false;
        return !(comments != null ? !comments.equals(that.comments) : that.comments != null);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (studies != null ? studies.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (authors != null ? authors.hashCode() : 0);
        result = 31 * result + (comments != null ? comments.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SurveyProgram{" +
                "studies=" + studies +
                ", description='" + description + '\'' +
                ", authors='" + authors + '\'' +
                ", comments=" + comments +
                "} " + super.toString();
    }
}
