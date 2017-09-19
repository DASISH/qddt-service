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
        if (user == null) return;
        setId(user.getId());
        setEmail(user.getEmail());
        setUsername(user.getUsername());
        if (user.getAgency() != null)
            setAgency(new AgencyJsonView(user.getAgency()));
    }


    public UUID getId() {
        return id;
    }

    private void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    private void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    private void setEmail(String email) {
        this.email = email;
    }

    public AgencyJsonView getAgency() {
        return agency;
    }

    private void setAgency(AgencyJsonView agency) {
        this.agency = agency;
    }
}
