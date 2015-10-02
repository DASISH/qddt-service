package no.nsd.qddt.domain.instrument;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.comment.Comment;
import no.nsd.qddt.domain.instrumentquestion.InstrumentQuestion;
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
public class Instrument extends AbstractEntityAudit {

    @ManyToOne
    @JoinColumn(name="study_id")
    private Study study;


    @OneToMany(mappedBy="instrument", cascade = CascadeType.ALL)
    private Set<InstrumentQuestion> instrumentQuestions = new HashSet<>();

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

    public Set<InstrumentQuestion> getInstrumentQuestions() {
        return instrumentQuestions;
    }

    public void setInstrumentQuestions(Set<InstrumentQuestion> instrumentQuestions) {
        this.instrumentQuestions = instrumentQuestions;
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
        if (!(o instanceof Instrument)) return false;
        if (!super.equals(o)) return false;

        Instrument that = (Instrument) o;

        if (getStudy() != null ? !getStudy().equals(that.getStudy()) : that.getStudy() != null) return false;
        if (getInstrumentQuestions() != null ? !getInstrumentQuestions().equals(that.getInstrumentQuestions()) : that.getInstrumentQuestions() != null)
            return false;
        return !(getComments() != null ? !getComments().equals(that.getComments()) : that.getComments() != null);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getStudy() != null ? getStudy().hashCode() : 0);
        result = 31 * result + (getInstrumentQuestions() != null ? getInstrumentQuestions().hashCode() : 0);
        result = 31 * result + (getComments() != null ? getComments().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Instrument{" +
                "study=" + study +
                ", instrumentQuestions=" + instrumentQuestions +
                ", comments=" + comments +
                '}';
    }
}
