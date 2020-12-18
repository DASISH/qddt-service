package no.nsd.qddt.domain.instruction.json;

import no.nsd.qddt.domain.instruction.Instruction;
import org.hibernate.annotations.Type;

import java.sql.Timestamp;
import java.util.UUID;

/**
 * @author Stig Norland
 */
public class InstructionJsonView {

    @Type(type="pg-uuid")
    private UUID id;

	private String name;
	
	private String description;

    private Timestamp modified;

    public InstructionJsonView() {
    }

    public InstructionJsonView(Instruction instruction) {
        if (instruction == null) return;
        setId(instruction.getId());
        setName(instruction.getName());
		setModified( instruction.getModified() );
		setDescription( instruction.getDescription() );
    }

	public String getDescription() {
		return this.description;
	}

    private void setDescription(String description) {
		this.description = description;
	}

	public UUID getId() {
        return id;
    }

    private void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    public Timestamp getModified() {
        return modified;
    }

    public void setModified(Timestamp modified) {
        this.modified = modified;
    }
}
