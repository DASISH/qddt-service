package no.nsd.qddt.security.user.json;

import no.nsd.qddt.domain.agency.AgencyJsonView;
import no.nsd.qddt.security.role.Authority;
import no.nsd.qddt.security.user.User;
import org.hibernate.PropertyNotFoundException;
import org.hibernate.annotations.Type;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Stig Norland
 */
public class UserJsonEdit {

    @Type(type="pg-uuid")
    private UUID id;

    private String name;

    private String email;

    private Set<Authority> authorities;

    private AgencyJsonView agency;

    private boolean enabled;

    private String classKind;

    private Timestamp modified;

    public UserJsonEdit() {
    }

    public UserJsonEdit(User user) {
        if (user == null) return;
        setId(user.getId());
        setEmail(user.getEmail());
        setName(user.getUsername());
        setAgency( new AgencyJsonView( user.getAgency()) );
        setAuthorities( user.getAuthorities().stream().findFirst().orElseThrow(
            () -> new PropertyNotFoundException(  "No Authority set " + user.getUsername() ) ) );
        setEnabled( user.isEnabled() );
        setClassKind( "USER" );
        setModified( user.getModified() );
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

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Authority authority) {
        this.authorities = Stream.of(authority)
            .collect( Collectors.toCollection(HashSet::new));
    }

    public AgencyJsonView getAgency() {
        return agency;
    }

    public void setAgency(AgencyJsonView agency) {
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

    public Timestamp getModified() {
        return modified;
    }

    public void setModified(Timestamp modified) {
        this.modified = modified;
    }
}
