package no.nsd.qddt.domain;

import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * <ul class="inheritance">
 *     <li>A Concept consist of one or more Questions.
 *     <ul class="inheritance">
 *         <li>Every Question will have a ResponseDomain.</li>
 *     </ul>
 * </li>
 * </ul>
 * </br>
 * ConceptScheme: Concepts express ideas associated with objects and means of representing the concept
 *
 * @author Stig Norland
 * @author Dag Østgulen Heradstveit
 */

@Audited
@Entity
@Table(name = "CONCEPT")
public class Concept extends AbstractEntityAudit {

    @ManyToOne
    @JoinColumn(name="topicgroup_id")
    private TopicGroup topicGroup;

    @OneToMany(mappedBy="parent", cascade = CascadeType.ALL)
    private Set<Concept> children = new HashSet<>();

    public Set<Concept> getChildren() {
        return children;
    }

    public void setChildren(Set<Concept> children) {
        this.children = children;
    }

    @OneToMany(mappedBy="question", cascade = CascadeType.ALL)
    private Set<Question> questions = new HashSet<>();

    public Set<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(Set<Question> questions) {
        this.questions = questions;
    }

    @Column(name = "label")
    private String label;

    @Column(name = "description")
    private String description;

    @Transient
    private Set<Comment> comments = new HashSet<>();

    public TopicGroup getTopicGroup() {
        return topicGroup;
    }

    public void setTopicGroup(TopicGroup topicGroup) {
        this.topicGroup = topicGroup;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


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



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Concept)) return false;
        if (!super.equals(o)) return false;

        Concept concept = (Concept) o;

        if (getTopicGroup() != null ? !getTopicGroup().equals(concept.getTopicGroup()) : concept.getTopicGroup() != null) return false;
        if (getChildren() != null ? !getChildren().equals(concept.getChildren()) : concept.getChildren() != null)
            return false;
        if (getLabel() != null ? !getLabel().equals(concept.getLabel()) : concept.getLabel() != null)
            return false;
        if (getDescription() != null ? !getDescription().equals(concept.getDescription()) : concept.getDescription() != null)
            return false;
        return !(getComments() != null ? !getComments().equals(concept.getComments()) : concept.getComments() != null);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getTopicGroup() != null ? getTopicGroup().hashCode() : 0);
        result = 31 * result + (getChildren() != null ? getChildren().hashCode() : 0);
        result = 31 * result + (getLabel() != null ? getLabel().hashCode() : 0);
        result = 31 * result + (getDescription() != null ? getDescription().hashCode() : 0);
        result = 31 * result + (getComments() != null ? getComments().hashCode() : 0);
        return result;
    }

    @Override
    /* tester ut latmanns to string, skulle funke det? */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(
                "Concept{" +
                "  module=" + topicGroup +
                ", label='" + label + '\'' +
                ", description='" + description + '\'' +
                ", comments=" + comments) ;
        sb.append(super.toString());
        sb.append('}');
        return sb.toString();
    }

}

