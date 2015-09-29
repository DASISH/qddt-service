package no.nsd.qddt.domain.instrument;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.Comment;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Stig Norland
 */
@Audited
@Entity
@Table(name = "instruction")
public class Instruction extends AbstractEntityAudit {

    public Instruction() {
    }

    @OneToMany(mappedBy = "instruction", cascade = CascadeType.ALL)
    private Set<InstrumentQuestion> instrumentQuestions = new HashSet<>();

    public Set<InstrumentQuestion> getInstrumentQuestions() {
        return instrumentQuestions;
    }

    public void setInstrumentQuestions(Set<InstrumentQuestion> instrumentQuestions) {
        this.instrumentQuestions = instrumentQuestions;
    }

    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    @OneToMany(cascade =CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name="owner_guid", foreignKey = @ForeignKey(name="guid"))
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Instruction)) return false;
        if (!super.equals(o)) return false;

        Instruction that = (Instruction) o;

        if (getInstrumentQuestions() != null ? !getInstrumentQuestions().equals(that.getInstrumentQuestions()) : that.getInstrumentQuestions() != null)
            return false;
        if (getDescription() != null ? !getDescription().equals(that.getDescription()) : that.getDescription() != null)
            return false;
        return !(getComments() != null ? !getComments().equals(that.getComments()) : that.getComments() != null);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getInstrumentQuestions() != null ? getInstrumentQuestions().hashCode() : 0);
        result = 31 * result + (getDescription() != null ? getDescription().hashCode() : 0);
        result = 31 * result + (getComments() != null ? getComments().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Instruction{" +
                "instrumentQuestions=" + instrumentQuestions.toString() +
                ", description='" + description + '\'' +
                ", comments=" + comments.toString() +
                '}';
    }
}
