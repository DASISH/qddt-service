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
@Table(name = "GROUP")
public class Group extends AbstractEntityAudit {

    @ManyToOne
    @JoinColumn(name="parent_id")
    private Concept parent;

    @OneToMany(mappedBy="parent", cascade = CascadeType.ALL)
    private Set<Concept> children = new HashSet<>();


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

// VAriabler er enda ikke tenkt på, men de kommer, da å denne være med...

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Group)) return false;
        if (!super.equals(o)) return false;

        Group group = (Group) o;

        if (getParent() != null ? !getParent().equals(group.getParent()) : group.getParent() != null) return false;
        if (getChildren() != null ? !getChildren().equals(group.getChildren()) : group.getChildren() != null)
            return false;
        if (getQuestions() != null ? !getQuestions().equals(group.getQuestions()) : group.getQuestions() != null)
            return false;
        if (getConcepts() != null ? !getConcepts().equals(group.getConcepts()) : group.getConcepts() != null)
            return false;
        if (getLabel() != null ? !getLabel().equals(group.getLabel()) : group.getLabel() != null) return false;
        return !(getDescription() != null ? !getDescription().equals(group.getDescription()) : group.getDescription() != null);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getParent() != null ? getParent().hashCode() : 0);
        result = 31 * result + (getChildren() != null ? getChildren().hashCode() : 0);
        result = 31 * result + (getQuestions() != null ? getQuestions().hashCode() : 0);
        result = 31 * result + (getConcepts() != null ? getConcepts().hashCode() : 0);
        result = 31 * result + (getLabel() != null ? getLabel().hashCode() : 0);
        result = 31 * result + (getDescription() != null ? getDescription().hashCode() : 0);
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


