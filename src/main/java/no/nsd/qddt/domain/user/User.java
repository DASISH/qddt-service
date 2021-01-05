package no.nsd.qddt.domain.user;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import no.nsd.qddt.domain.agency.Agency;
import no.nsd.qddt.domain.role.Authority;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import static no.nsd.qddt.utils.StringTool.IsNullOrTrimEmpty;
import static no.nsd.qddt.utils.StringTool.SafeString;

/**
 * @author Dag Østgulen Heradstveit
 * @author Stig Norland
 */


@Entity
@Audited
@Table(name = "USER_ACCOUNT", uniqueConstraints = { @UniqueConstraint(columnNames = {"email" },name = "UNQ_USER_EMAIL") } )
public class User  {

    @Transient
    @JsonIgnore
    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Id
    @Type(type="pg-uuid")
    @GeneratedValue(generator ="UUID")
    @GenericGenerator(name ="UUID", strategy ="org.hibernate.id.UUIDGenerator")
    @Column(name ="id", updatable = false, nullable = false)
    private UUID id;

    @JsonIgnore
    @Column(name = "username")
    private String username;

    @Column(name = "username", updatable = false ,insertable = false)
    private String name;


    @ManyToOne(fetch = FetchType.EAGER)
    @JsonBackReference(value = "agentRef")
    @JoinColumn(name = "agency_id")
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private Agency agency;

    @JsonIgnore
    @Column(name = "password", updatable = false)
    private String password;

    @Column(name = "email")
    private String email;

    @JsonIgnore
    @Column(name = "is_enabled")
    private boolean isEnabled;

    @Column(name = "updated")
    @Version
    private Timestamp modified;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="user_authority",
            joinColumns=@JoinColumn(name="user_id"),
            inverseJoinColumns=@JoinColumn(name="authority_id"))
    private Set<Authority> authorities = new HashSet<>();


    public User() {
    }

    public User(String name, String email, String password) {
        setUsername(name);
        setEmail(email);
        setPassword(password);
    }

    @PrePersist
    public void onPrePersist() {
        LOG.debug("USER INSERT");
        if (IsNullOrTrimEmpty( password))
            setPassword("$2a$10$O1MMi3SLcvwtJIT9CSZyN.aLtFKN.K2LtKyHZ52wElo0zh5gI1EyW");    // set password = password
    }

    @PreUpdate
    public void onPreUpdate() {
        LOG.debug("USER UPDATE");
    }


    public UUID getId() {
        if (id == null)
            LOG.error( "user id is null " + this);
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return SafeString(username);
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return SafeString(username);
    }

    public void setName(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public Timestamp getModified() {
        return modified;
    }

    public void setModified(Timestamp modified) {
        this.modified = modified;
    }

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }


    public Agency getAgency() {
        return agency;
    }

    public void setAgency(Agency agency) {
        this.agency = agency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (isEnabled != user.isEnabled) return false;
        if (!Objects.equals( id, user.id )) return false;
        if (!Objects.equals( name, user.name )) return false;
        if (!Objects.equals( agency, user.agency )) return false;
        if (!Objects.equals( password, user.password )) return false;
        if (!Objects.equals( email, user.email )) return false;
        return Objects.equals( modified, user.modified );
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (agency != null ? agency.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (isEnabled ? 1 : 0);
        result = 31 * result + (modified != null ? modified.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return " \"User \" : \"" + getUsername() + "@" + (agency!=null ? agency.getName(): "?") + ":->" + getModified() +", ";
    }

}
