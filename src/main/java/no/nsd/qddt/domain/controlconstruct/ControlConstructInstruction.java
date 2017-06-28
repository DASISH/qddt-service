package no.nsd.qddt.domain.controlconstruct;

import no.nsd.qddt.domain.instruction.Instruction;
import org.hibernate.envers.Audited;

import javax.persistence.*;

/**
 * @author Stig Norland
 */

@Audited
@Embeddable
public class ControlConstructInstruction {


    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE})
    @JoinColumn(name = "instruction_id")
    private Instruction instruction;

    @Enumerated(EnumType.STRING)
    private ControlConstructInstructionRank instructionRank;


    public Instruction getInstruction() {
        return instruction;
    }

    public void setInstruction(Instruction instruction) {
        this.instruction = instruction;
    }

    public ControlConstructInstructionRank getInstructionRank() {
        return instructionRank;
    }

    public void setInstructionRank(ControlConstructInstructionRank instructionRank) {
        this.instructionRank = instructionRank;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ControlConstructInstruction)) return false;

        ControlConstructInstruction that = (ControlConstructInstruction) o;

        return (instruction != null ? instruction.equals(that.instruction) : that.instruction == null) && instructionRank == that.instructionRank;

    }

    @Override
    public int hashCode() {
        int result = (instruction != null ? instruction.hashCode() : 0);
        result = 31 * result + (instructionRank != null ? instructionRank.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ControlConstructInstruction{" +
                ", instruction=" + instruction +
                ", instructionRank=" + instructionRank +
                '}';
    }


}
