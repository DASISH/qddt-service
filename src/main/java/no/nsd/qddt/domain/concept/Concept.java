package no.nsd.qddt.domain.concept;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.comment.Comment;
import no.nsd.qddt.domain.question.Question;
import no.nsd.qddt.domain.topicgroup.TopicGroup;
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

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="parent_id")
    private Concept parent;

    @OneToMany(mappedBy="parent", cascade = CascadeType.ALL)
    private Set<Concept> children = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "question_concept", joinColumns = {
            @JoinColumn(name = "concept_id", nullable = false, updatable = false) },
            inverseJoinColumns = { @JoinColumn(name = "question_id",
                    nullable = false, updatable = false) })
    private Set<Question> questions = new HashSet<>();

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

    public Concept getParent() {
        return parent;
    }

    public void setParent(Concept parent) {
        this.parent = parent;
    }

    public Set<Concept> getChildren() {
        return children;
    }

    public void setChildren(Set<Concept> children) {
        this.children = children;
    }

    public Set<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(Set<Question> questions) {
        this.questions = questions;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Concept concept = (Concept) o;

        if (label != null ? !label.equals(concept.label) : concept.label != null) return false;
        return !(description != null ? !description.equals(concept.description) : concept.description != null);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (label != null ? label.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Concept{" +
                "description='" + description + '\'' +
                ", label='" + label + '\'' +
                '}';
    }
}

