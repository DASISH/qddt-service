package no.nsd.qddt.domain.group;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.comment.Comment;
import no.nsd.qddt.domain.concept.Concept;
import no.nsd.qddt.domain.question.Question;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Stig Norland
 */
@Audited
@Entity
@Table(name = "GROUP_")
public class Group extends AbstractEntityAudit {

    @ManyToOne
    @JoinColumn(name="parent_id")
    private Group parent;

    @OneToMany(mappedBy="parent", cascade = CascadeType.ALL)
    private Set<Group> children = new HashSet<>();


    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "GROUP_QUESTION",
            joinColumns = {@JoinColumn(name ="group_id", nullable = false, updatable = false)},
            inverseJoinColumns = {@JoinColumn(name = "question_id", nullable = false,updatable = false)})
    private Set<Question> questions = new HashSet<>();


    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "GROUP_CONCEPT",
            joinColumns = {@JoinColumn(name ="group_id", nullable = false, updatable = false)},
            inverseJoinColumns = {@JoinColumn(name = "concept_id", nullable = false,updatable = false)})
    private Set<Concept> concepts = new HashSet<>();

// TODO implement variables
//    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    @JoinTable(name = "GROUP_VARIABLE",
//            joinColumns = {@JoinColumn(name ="group_id", nullable = false, updatable = false)},
//            inverseJoinColumns = {@JoinColumn(name = "variabel_id", nullable = false,updatable = false)})
//    private Set<Varialbe> varialbes = new HashSet<>();

    @Column(name = "label")
    private String label;

    @Column(name = "description")
    private String description;

    @Transient
    private Set<Comment> comments = new HashSet<>();


    public Group(){

    }


    public Group getParent() {
        return parent;
    }

    public void setParent(Group parent) {
        this.parent = parent;
    }

    public Set<Group> getChildren() {
        return children;
    }

    public void setChildren(Set<Group> children) {
        this.children = children;
    }

    public Set<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(Set<Question> questions) {
        this.questions = questions;
    }

    public Set<Concept> getConcepts() {
        return concepts;
    }

    public void setConcepts(Set<Concept> concepts) {
        this.concepts = concepts;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Group)) return false;
        if (!super.equals(o)) return false;

        Group group = (Group) o;

        if (parent != null ? !parent.equals(group.parent) : group.parent != null) return false;
        if (children != null ? !children.equals(group.children) : group.children != null) return false;
        if (questions != null ? !questions.equals(group.questions) : group.questions != null)
            return false;
        if (concepts != null ? !concepts.equals(group.concepts) : group.concepts != null)
            return false;
        if (label != null ? !label.equals(group.label) : group.label != null) return false;
        return !(description != null ? !description.equals(group.description) : group.description != null);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (parent != null ? parent.hashCode() : 0);
        result = 31 * result + (children != null ? children.hashCode() : 0);
        result = 31 * result + (questions != null ? questions.hashCode() : 0);
        result = 31 * result + (concepts != null ? concepts.hashCode() : 0);
        result = 31 * result + (label != null ? label.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Group{" +
                ", label='" + label + '\'' +
                ", description='" + description + '\'' +
                "} " + super.toString();
    }
}


