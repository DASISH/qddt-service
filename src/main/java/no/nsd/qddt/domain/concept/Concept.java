package no.nsd.qddt.domain.concept;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.author.Author;
import no.nsd.qddt.domain.authorable.Authorable;
import no.nsd.qddt.domain.comment.Comment;
import no.nsd.qddt.domain.commentable.Commentable;
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
public class Concept extends AbstractEntityAudit implements Commentable, Authorable {

    @ManyToOne
    @JoinColumn(name="parent_id")
    private Concept parent;

    @OneToMany(mappedBy="parent", cascade = CascadeType.ALL)
    private Set<Concept> children = new HashSet<>();

    @ManyToOne
    @JoinColumn(name="topicgroup_id")
    private TopicGroup topicGroup;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "CONCEPT_QUESTION",
            joinColumns = {@JoinColumn(name ="concept_id", nullable = false, updatable = false)},
            inverseJoinColumns = {@JoinColumn(name = "question_id", nullable = false,updatable = false)})
    private Set<Question> questions = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "concepts", cascade = CascadeType.ALL)
    private Set<Author> authors = new HashSet<>();

    @Column(name = "label")
    private String label;

    @Column(name = "description")
    private String description;

    @Transient
    private Set<Comment> comments = new HashSet<>();

    public Concept(){

    }

    public Concept getParent() {
        return parent;
    }

    public void setParent(Concept parent) {
        this.parent = parent;
    }

    public TopicGroup getTopicGroup() {
        return topicGroup;
    }

    public void setTopicGroup(TopicGroup topicGroup) {
        this.topicGroup = topicGroup;
    }

    public Set<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(Set<Question> questions) {
        this.questions = questions;
    }

    public Set<Concept> getChildren() {
        return children;
    }

    public void setChildren(Set<Concept> children) {
        this.children = children;
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
        comment.setOwnerId(this.getId());
        comments.add(comment);
    }


    @Override
    public void addAuthor(Author user) {
        authors.add(user);
    }

    @Override
    public Set<Author> getAuthors() {
        return authors;
    }

    @Override
    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Concept)) return false;
        if (!super.equals(o)) return false;

        Concept concept = (Concept) o;

        if (parent != null ? !parent.equals(concept.parent) : concept.parent != null) return false;
        if (children != null ? !children.equals(concept.children) : concept.children != null) return false;
        if (topicGroup != null ? !topicGroup.equals(concept.topicGroup) : concept.topicGroup != null) return false;
        if (questions != null ? !questions.equals(concept.questions) : concept.questions != null) return false;
        if (label != null ? !label.equals(concept.label) : concept.label != null) return false;
        if (description != null ? !description.equals(concept.description) : concept.description != null) return false;
        return !(comments != null ? !comments.equals(concept.comments) : concept.comments != null);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (parent != null ? parent.hashCode() : 0);
        result = 31 * result + (children != null ? children.hashCode() : 0);
        result = 31 * result + (topicGroup != null ? topicGroup.hashCode() : 0);
        result = 31 * result + (questions != null ? questions.hashCode() : 0);
        result = 31 * result + (label != null ? label.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (comments != null ? comments.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Concept{" +
                "label='" + label + '\'' +
                ", description='" + description + '\'' +
                "} " + super.toString();
    }

}

