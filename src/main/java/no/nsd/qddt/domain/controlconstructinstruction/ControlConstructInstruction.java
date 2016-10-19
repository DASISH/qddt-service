package no.nsd.qddt.domain.controlconstructinstruction;

import no.nsd.qddt.domain.AbstractEntity;
import no.nsd.qddt.domain.controlconstruct.ControlConstruct;
import no.nsd.qddt.domain.instruction.Instruction;
import org.hibernate.envers.Audited;

import javax.persistence.*;

/**
 * @author Stig Norland
 */

@Audited
@Entity
//@IdClass(CCIKey.class)
@Table(name = "CONTROL_CONSTRUCT_INSTRUCTION")
public class ControlConstructInstruction extends AbstractEntity {

    @ManyToOne(cascade = {CascadeType.MERGE,CascadeType.DETACH,CascadeType.PERSIST})
    @JoinColumn(name = "controlConstruct_id")
    private ControlConstruct controlConstruct;

    @ManyToOne(cascade = {CascadeType.MERGE,CascadeType.DETACH,CascadeType.PERSIST})
    @JoinColumn(name = "instruction_id")
    private Instruction instruction;

    @Enumerated(EnumType.STRING)
    private  InstructionRank instructionRank;

    public ControlConstruct getControlConstruct() {
        return controlConstruct;
    }

    public void setControlConstruct(ControlConstruct controlConstruct) {
        this.controlConstruct = controlConstruct;
    }

    public Instruction getInstruction() {
        return instruction;
    }

    public void setInstruction(Instruction instruction) {
        this.instruction = instruction;
    }

    public InstructionRank getInstructionRank() {
        return instructionRank;
    }

    public void setInstructionRank(InstructionRank instructionRank) {
        this.instructionRank = instructionRank;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ControlConstructInstruction)) return false;

        ControlConstructInstruction that = (ControlConstructInstruction) o;

        if (controlConstruct != null ? !controlConstruct.equals(that.controlConstruct) : that.controlConstruct != null)
            return false;
        if (instruction != null ? !instruction.equals(that.instruction) : that.instruction != null) return false;
        return instructionRank == that.instructionRank;

    }

    @Override
    public int hashCode() {
        int result = controlConstruct != null ? controlConstruct.hashCode() : 0;
        result = 31 * result + (instruction != null ? instruction.hashCode() : 0);
        result = 31 * result + (instructionRank != null ? instructionRank.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ControlConstructInstruction{" +
                "controlConstruct=" + controlConstruct +
                ", instruction=" + instruction +
                ", instructionRank=" + instructionRank +
                '}';
    }
}
