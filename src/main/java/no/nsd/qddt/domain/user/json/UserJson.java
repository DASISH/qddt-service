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

    private String username;

    private String email;

    private String agencyUserName;

    private Timestamp modified;

    public UserJson() {
    }

    public UserJson(User user) {
        if (user == null) return;
        setId(user.getId());
        setEmail(user.getEmail());
        setUsername(user.getUsername());
        setAgencyUserName(username + "@" + user.getAgency().getName());
        setModified( user.getModified() );
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

    /**
     * @param agencyUserName the agencyUserName to set
     */
    public void setAgencyUserName(String agencyUserName) {
        this.agencyUserName = agencyUserName;
    }

    /**
     * @return the agencyUserName
     */
    public String getAgencyUserName() {
        return agencyUserName;
    }

    public Timestamp getModified() {
        return modified;
    }

    public void setModified(Timestamp modified) {
        this.modified = modified;
    }
}
