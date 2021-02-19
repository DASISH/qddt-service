package no.nsd.qddt.domain.instruction.json;

import no.nsd.qddt.domain.AbstractJsonEdit;
import no.nsd.qddt.domain.instruction.Instruction;

/**
 * @author Stig Norland
 */
public class InstructionJsonEdit extends AbstractJsonEdit {

    private static final long serialVersionUID = -2195696372853764486L;


    private String description;


    public InstructionJsonEdit() {
    }

    public InstructionJsonEdit(Instruction instruction) {
        super(instruction);
        setDescription(instruction.getDescription());
    }


    public String getDescription() {
        return description;
    }

    private void setDescription(String description) {
        this.description = description;
    }

}
