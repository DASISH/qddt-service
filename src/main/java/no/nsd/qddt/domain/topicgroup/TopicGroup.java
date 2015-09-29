package no.nsd.qddt.domain.topicgroup;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.comment.Comment;
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
@Table(name = "topic_group")
public class TopicGroup extends AbstractEntityAudit {

    @ManyToOne
    @JoinColumn(name="study_id")
    private Study study;

    @ManyToOne
    @JoinColumn(name = "concept_id")
    private Concept concept;

    @OneToMany
    @JoinColumn(name="author_id")
    private List<User> authors;

    @OneToMany(cascade =CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "guid")
    private List<OtherMaterial> otherMaterials;

    private String authorsAffiliation;

    private String Abstract;

    @Transient
    private Set<Comment> comments = new HashSet<>();

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public void addComment(Comment comment) {
        comment.setOwnerUUID(this.getId());
        comments.add(comment);
    }


    public Study getStudy() {
        return study;
    }

    public void setStudy(Study study) {
        this.study = study;
    }

    public Concept getConcept() {
        return concept;
    }

    public void setConcept(Concept concept) {
        this.concept = concept;
    }

    public List<User> getAuthors() {
        return authors;
    }

    public void setAuthors(List<User> authors) {
        this.authors = authors;
    }

    public List<OtherMaterial> getOtherMaterials() {
        return otherMaterials;
    }

    public void setOtherMaterials(List<OtherMaterial> otherMaterials) {
        this.otherMaterials = otherMaterials;
    }

    public String getAuthorsAffiliation() {
        return authorsAffiliation;
    }

    public void setAuthorsAffiliation(String authorsAffiliation) {
        this.authorsAffiliation = authorsAffiliation;
    }

    public String getAbstract() {
        return Abstract;
    }

    public void setAbstract(String anAbstract) {
        this.Abstract = anAbstract;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        TopicGroup topicGroup = (TopicGroup) o;

        if (study != null ? !study.equals(topicGroup.study) : topicGroup.study != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (study != null ? study.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Module{" +
                "study=" + study +
                super.toString() +
                '}';
    }
}
