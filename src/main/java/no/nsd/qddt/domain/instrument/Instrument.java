package no.nsd.qddt.domain.instrument;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.comment.Comment;
import no.nsd.qddt.domain.commentable.Commentable;
import no.nsd.qddt.domain.controlconstruct.ControlConstruct;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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


    @OneToMany(mappedBy="instrument", cascade = CascadeType.ALL)
    @OrderColumn(name="controlConstruct_idx")
    private List<ControlConstruct> controlConstructs =new ArrayList<>();

    @Transient
    private Set<Comment> comments = new HashSet<>();

    public Instrument() {
    }


    public List<ControlConstruct> getControlConstructs() {
        return controlConstructs;
    }

    public void setControlConstructs(List<ControlConstruct> controlConstructs) {
        this.controlConstructs = controlConstructs;
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

        return !(comments != null ? !comments.equals(that.comments) : that.comments != null);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (comments != null ? comments.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Instrument{" +
//                "study=" + study +
                "} " + super.toString();
    }
}
