package no.nsd.qddt.domain.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Objects;
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
import java.util.Set;
import java.util.UUID;

import static no.nsd.qddt.utils.StringTool.SafeString;

/**
 * @author Dag Østgulen Heradstveit
 * @author Stig Norland
 */


@Entity
@Audited
@Table(name = "USER_ACCOUNT", uniqueConstraints = {@UniqueConstraint(columnNames = {"email" },name = "UNQ_USER_EMAIL")})
public class User  {

    @Transient
    @JsonIgnore
    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Id
    @Type(type="pg-uuid")
    @Column(name = "id")
    @GenericGenerator(name = "uuid-gen", strategy = "uuid2")
    @GeneratedValue(generator = "uuid-gen")
    private UUID id;

    @JsonIgnore
    @Column(name = "username")
    private String username;

    @Column(name = "username", updatable = false ,insertable = false)
    private String name;


    @ManyToOne(fetch = FetchType.EAGER)
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

//    public User(UserJsonEdit instance) {
//        setId( instance.getId() );
//        setUsername( instance.getName() );
//        setEmail( instance.getEmail() );
//        setEnabled( instance.isEnabled() );
//        setAuthorities( new HashSet<>( Arrays.asList( instance.getAuthority()) ));
//        setAgency( instance.getAgency() );
//        setModified( instance.getModified() );
//    }

    @PrePersist
    public void onPrePersist() {
        LOG.debug("USER INSERT");
        setPassword("$2a$10$O1MMi3SLcvwtJIT9CSZyN.aLtFKN.K2LtKyHZ52wElo0zh5gI1EyW");    // set password = password
    }

    @PreUpdate
    public void onPreUpdate() {
        LOG.debug("USER UPDATE");
    }


    public UUID getId() {
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
        return Objects.equal( id, user.id ) &&
            Objects.equal( email, user.email ) &&
            Objects.equal( modified, user.modified );
    }

    @Override
    public int hashCode() {
        return Objects.hashCode( id, email, modified );
    }

    @Override
    public String toString() {
        return " \"User \" { \"name\" : \"" + getUsername() + "@" + (agency!=null ? agency.getName(): "?") + "\" } ";
    }

}
