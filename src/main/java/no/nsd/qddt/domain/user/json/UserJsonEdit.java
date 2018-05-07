package no.nsd.qddt.domain.user.json;

import no.nsd.qddt.domain.agency.Agency;
import no.nsd.qddt.domain.role.Authority;
import no.nsd.qddt.domain.user.User;
import org.hibernate.PropertyNotFoundException;
import org.hibernate.annotations.Type;

import java.util.UUID;

/**
 * @author Stig Norland
 */
public class UserJsonEdit {

    @Type(type="pg-uuid")
    private UUID id;

    private String name;

    private String email;

    private Authority authority;

    private Agency agency;

    private boolean enabled;

    private String classKind;

    public UserJsonEdit() {
    }

    public UserJsonEdit(User user) {
        if (user == null) return;
        setId(user.getId());
        setEmail(user.getEmail());
        setName(user.getUsername());
        setAgency( user.getAgency() );
        setAuthority( user.getAuthorities().stream().findFirst().orElseThrow( () -> new PropertyNotFoundException( "Authority" ) ) );
        setEnabled( user.isEnabled() );
        setClassKind( "USER" );
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

    public Authority getAuthority() {
        return authority;
    }

    public void setAuthority(Authority authority) {
        this.authority =  authority;
    }

    public Agency getAgency() {
        return agency;
    }

    public void setAgency(Agency agency) {
        this.agency = agency;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getClassKind() {
        return classKind;
    }

    public void setClassKind(String classKind) {
        this.classKind = classKind;
    }
}
