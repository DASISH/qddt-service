package no.nsd.qddt.domain.topicgroup;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.comment.Comment;
import no.nsd.qddt.domain.commentable.Commentable;
import no.nsd.qddt.domain.concept.Concept;
import no.nsd.qddt.domain.othermaterial.OtherMaterial;
import no.nsd.qddt.domain.study.Study;
import no.nsd.qddt.domain.user.User;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * <ul class="inheritance">
 *         <li>A Module will have one or more Concepts.
 *         <ul class="inheritance">
 *             <li>A Concept consist of one or more Questions.</li>
 *             <ul class="inheritance">
 *                 <li>Every Question will have a ResponseDomain.</li>
 *             </ul>
 *          </ul>
 *      </li>
 * </ul>
 * </br>
 * A Module should be a collection of Questions and Concepts that has a theme that is broader than a Concept.
 * All Questions that doesn't belong to a specific Concept, will be collected in a default Concept that
 * every Module should have. This default Concept should not be visualized as a Concept, but as a
 * "Virtual Module". The reason for this is a simplified data model.
 *
 * @author Stig Norland
 * @author Dag Østgulen Heradstveit
 */

@Audited
@Entity
@Table(name = "TOPIC_GROUP")
public class TopicGroup extends AbstractEntityAudit implements Commentable {

    @ManyToOne
    @JoinColumn(name="study_id")
    private Study study;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "topicGroup")
    private Set<Concept> concepts = new HashSet<>();

    @OneToMany
    @JoinColumn(name="author_id")
    private Set<User> authors = new HashSet<>();

    @OneToMany(mappedBy = "topicGroup", cascade =CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<OtherMaterial> otherMaterials = new HashSet<>();

    private String authorsAffiliation;

    private String abstractDescription;

    @Transient
    private Set<Comment> comments = new HashSet<>();

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

    public Study getStudy() {
        return study;
    }

    public void setStudy(Study study) {
        this.study = study;
    }

    public Set<Concept> getConcepts() {
        return concepts;
    }

    public void setConcepts(Set<Concept> concepts) {
        this.concepts = concepts;
    }

    public Set<User> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<User> authors) {
        this.authors = authors;
    }

    public Set<OtherMaterial> getOtherMaterials() {
        return otherMaterials;
    }

    public void setOtherMaterials(Set<OtherMaterial> otherMaterials) {
        this.otherMaterials = otherMaterials;
    }

    public String getAuthorsAffiliation() {
        return authorsAffiliation;
    }

    public void setAuthorsAffiliation(String authorsAffiliation) {
        this.authorsAffiliation = authorsAffiliation;
    }

    public String getAbstractDescription() {
        return abstractDescription;
    }

    public void setAbstractDescription(String abstractDescription) {
        this.abstractDescription = abstractDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TopicGroup)) return false;
        if (!super.equals(o)) return false;

        TopicGroup that = (TopicGroup) o;

        if (study != null ? !study.equals(that.study) : that.study != null) return false;
        if (authors != null ? !authors.equals(that.authors) : that.authors != null) return false;
        if (otherMaterials != null ? !otherMaterials.equals(that.otherMaterials) : that.otherMaterials != null)
            return false;
        if (authorsAffiliation != null ? !authorsAffiliation.equals(that.authorsAffiliation) : that.authorsAffiliation != null)
            return false;
        if (abstractDescription != null ? !abstractDescription.equals(that.abstractDescription) : that.abstractDescription != null) return false;
        return !(comments != null ? !comments.equals(that.comments) : that.comments != null);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (study != null ? study.hashCode() : 0);
        result = 31 * result + (concepts != null ? concepts.hashCode() : 0);
        result = 31 * result + (authors != null ? authors.hashCode() : 0);
        result = 31 * result + (otherMaterials != null ? otherMaterials.hashCode() : 0);
        result = 31 * result + (authorsAffiliation != null ? authorsAffiliation.hashCode() : 0);
        result = 31 * result + (abstractDescription != null ? abstractDescription.hashCode() : 0);
        result = 31 * result + (comments != null ? comments.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "TopicGroup{" +
                "study=" + study +
                ", concepts=" + concepts +
                ", authors=" + authors +
                ", otherMaterials=" + otherMaterials +
                ", authorsAffiliation='" + authorsAffiliation + '\'' +
                ", abstractDescription='" + abstractDescription + '\'' +
                ", comments=" + comments +
                "} " + super.toString();
    }
}