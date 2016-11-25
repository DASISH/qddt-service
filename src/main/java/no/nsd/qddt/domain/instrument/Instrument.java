package no.nsd.qddt.domain.instrument;

import com.fasterxml.jackson.annotation.JsonBackReference;
import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.comment.Comment;
import no.nsd.qddt.domain.commentable.Commentable;
import no.nsd.qddt.domain.concept.Concept;
import no.nsd.qddt.domain.controlconstruct.ControlConstruct;
import no.nsd.qddt.domain.study.Study;
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


    @JsonBackReference(value = "studyRef")
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "instruments")
    private Set<Study> studies = new HashSet<>();

    //TODO ArrayList dosn't work with Enver
    @OneToMany(mappedBy="instrument", cascade = CascadeType.ALL)
    @OrderColumn(name="controlConstruct_idx")
    private List<ControlConstruct> controlConstructs =new ArrayList<>();

    private String description;

    private String instrumentKind;


    @Transient
    private Set<Comment> comments = new HashSet<>();

    public Instrument() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInstrumentKind() {
        return instrumentKind;
    }

    public void setInstrumentKind(String instrumentKind) {
        this.instrumentKind = instrumentKind;
    }

    public Set<Study> getStudies() {
        return studies;
    }

    public void setStudies(Set<Study> studies) {
        this.studies = studies;
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

        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        return instrumentKind != null ? instrumentKind.equals(that.instrumentKind) : that.instrumentKind == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (instrumentKind != null ? instrumentKind.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Instrument{" +
                "description='" + description + '\'' +
                ", instrumentKind='" + instrumentKind + '\'' +
                "} " + super.toString();
    }
}
