package no.nsd.qddt.domain.instrument;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.comment.Comment;
import no.nsd.qddt.domain.commentable.Commentable;
import no.nsd.qddt.domain.controlconstruct.ControlConstruct;
import no.nsd.qddt.domain.study.Study;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * You change your meaning by emphasizing different words in your sentence. ex: "I never said she stole my money" has 7 meanings.
 *
 * @author Stig Norland
 * @author Dag Østgulen Heradstveit
 */

@Audited
@Entity
@Table(name = "INSTRUMENT")
public class Instrument extends AbstractEntityAudit implements Commentable {

    @ManyToOne
    @JoinColumn(name="parent_id")
    private Instrument parent;

    @OneToMany(mappedBy="parent", cascade = CascadeType.ALL)
    private Set<Instrument> children = new HashSet<>();

    @ManyToOne
    @JoinColumn(name="study_id")
    private Study study;


    @OneToMany(mappedBy="instrument", cascade = CascadeType.ALL)
    private Set<ControlConstruct> controlConstructs = new HashSet<>();

    @Transient
    private Set<Comment> comments = new HashSet<>();

    public Instrument() {
    }

    public Study getStudy() {
        return study;
    }

    public void setStudy(Study study) {
        this.study = study;
    }

    public Set<ControlConstruct> getControlConstructs() {
        return controlConstructs;
    }

    public void setControlConstructs(Set<ControlConstruct> controlConstructs) {
        this.controlConstructs = controlConstructs;
    }

    public Instrument getParent() {
        return parent;
    }

    public void setParent(Instrument parent) {
        this.parent = parent;
    }

    public Set<Instrument> getChildren() {
        return children;
    }

    public void setChildren(Set<Instrument> children) {
        this.children = children;
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
        if (!(o instanceof Instrument)) return false;
        if (!super.equals(o)) return false;

        Instrument that = (Instrument) o;

        if (study != null ? !study.equals(that.study) : that.study != null) return false;
        return !(comments != null ? !comments.equals(that.comments) : that.comments != null);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (study != null ? study.hashCode() : 0);
        result = 31 * result + (comments != null ? comments.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Instrument{" +
                "study=" + study +
                "} " + super.toString();
    }
}
