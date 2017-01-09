package no.nsd.qddt.domain.agency;

import org.hibernate.annotations.Type;

import java.util.UUID;

/**
 * @author Stig Norland
 */
public class AgencyJsonView {

    @Type(type="pg-uuid")
    private UUID id;

    private String name;

    public AgencyJsonView() {
    }

    public AgencyJsonView(Agency agency) {
        setId(agency.getId());
        setName(agency.getName());
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
