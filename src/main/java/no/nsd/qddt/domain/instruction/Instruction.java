package no.nsd.qddt.domain.instruction;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.comment.Comment;
import no.nsd.qddt.domain.commentable.Commentable;
import no.nsd.qddt.domain.controlconstruct.ControlConstruct;
import no.nsd.qddt.domain.controlconstructinstruction.ControlConstructInstruction;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Stig Norland
 */
@Audited
@Entity
@Table(name = "INSTRUCTION")
public class Instruction extends AbstractEntityAudit implements Commentable {

    @JsonBackReference(value = "controlConstructInstructionRef")
    @OneToMany(mappedBy = "instruction")
    private List<ControlConstructInstruction> controlConstructInstructions =new ArrayList<>();


    @Column(name = "description", length = 2000)
    private String description;

    @Transient
    private Set<Comment> comments = new HashSet<>();


    public Instruction() {
    }


    public List<ControlConstructInstruction> getControlConstructInstructions() {
        return controlConstructInstructions;
    }

    public void setControlConstructInstructions(List<ControlConstructInstruction> controlConstructInstructions) {
        this.controlConstructInstructions = controlConstructInstructions;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (getName().isEmpty())
            this.setName(description.toUpperCase().substring(0,25));

        this.description = description;
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
        if (!(o instanceof Instruction)) return false;
        if (!super.equals(o)) return false;

        Instruction that = (Instruction) o;

        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        return !(comments != null ? !comments.equals(that.comments) : that.comments != null);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (comments != null ? comments.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Instruction{" +
                ", description='" + description + '\'' +
                "} " + super.toString();
    }
}
