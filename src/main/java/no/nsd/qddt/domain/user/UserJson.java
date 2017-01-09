package no.nsd.qddt.domain.user;

import no.nsd.qddt.domain.agency.AgencyJsonView;
import org.hibernate.annotations.Type;

import java.util.UUID;

/**
 * @author Stig Norland
 */
public class UserJson {

    @Type(type="pg-uuid")
    private UUID id;

    private String username;

    private String email;

    private AgencyJsonView agency;

    public UserJson() {
    }

    public UserJson(User user) {
        setId(user.getId());
        setAgency(new AgencyJsonView(user.getAgency()));
        setEmail(user.getEmail());
        setUsername(user.getUsername());
    }


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public AgencyJsonView getAgency() {
        return agency;
    }

    public void setAgency(AgencyJsonView agency) {
        this.agency = agency;
    }
}
