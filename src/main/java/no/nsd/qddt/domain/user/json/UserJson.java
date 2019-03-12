package no.nsd.qddt.domain.user.json;

import no.nsd.qddt.domain.user.User;
import org.hibernate.annotations.Type;

import java.sql.Timestamp;
import java.util.UUID;

/**
 * @author Stig Norland
 */
public class UserJson {

    @Type(type="pg-uuid")
    private UUID id;

    private String name;

    private String email;

    private Timestamp modified;

    private String agencyName;

    public UserJson() {
    }

    public UserJson(User user) {
        if (user == null) return;
        setId(user.getId());
        setEmail(user.getEmail());
        setName(user.getUsername());
        setModified( user.getModified() );
        setAgencyName( (user.getAgency() != null) ? user.getAgency().getName() : "IS NULL" );
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

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    private void setEmail(String email) {
        this.email = email;
    }

    public Timestamp getModified() {
        return modified;
    }

    private void setModified(Timestamp modified) {
        this.modified = modified;
    }

    public String getAgencyName() {
        return agencyName;
    }

    private void setAgencyName(String agencyName) {
        this.agencyName = agencyName;
    }
}
