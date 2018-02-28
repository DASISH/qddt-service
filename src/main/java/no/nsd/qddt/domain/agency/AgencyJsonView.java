package no.nsd.qddt.domain.agency;

import org.hibernate.annotations.Type;

import java.sql.Timestamp;
import java.util.UUID;

/**
 * @author Stig Norland
 */
public class AgencyJsonView {

    @Type(type="pg-uuid")
    private UUID id;

    private String name;

    private Timestamp modified;

    public AgencyJsonView() {
    }

    public AgencyJsonView(Agency agency) {
        if (agency == null) return;
        setId(agency.getId());
        setName(agency.getName());
        setModified( agency.getModified() );
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
