package no.nsd.qddt.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import no.nsd.qddt.domain.security.Authority;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;
import org.hibernate.envers.RevisionEntity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Dag Østgulen Heradstveit
 */
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@Entity
@Table(name = "user")
public class User extends AbstractEntity {

    @Column(name = "username")
    private String username;

    @JsonIgnore
    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name="user_authority",
            joinColumns=@JoinColumn(name="user_id"),
            inverseJoinColumns=@JoinColumn(name="authority_id"))
    private Set<Authority> authorities = new HashSet<>();

    @OneToMany(mappedBy="createdBy", cascade = CascadeType.ALL)
    private Set<Survey> surveys = new HashSet<>();

    @OneToMany(mappedBy="createdBy", cascade = CascadeType.ALL)
    private Set<Study> studies = new HashSet<>();

    @OneToMany(mappedBy="createdBy", cascade = CascadeType.ALL)
    private Set<Comment> comments = new HashSet<>();

    public User() {
    }

    public User(String email, String password, String username) {
        this.email = email;
        this.password = password;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
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

    public Set<Authority> getAuthorities() {
        return new HashSet<>(authorities);
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }

    public Set<Survey> getSurveys() {
        return surveys;
    }

    public void setSurveys(Set<Survey> surveys) {
        this.surveys = surveys;
    }

    public Set<Study> getStudies() {
        return studies;
    }

    public void setStudies(Set<Study> studies) {
        this.studies = studies;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return !(email != null ? !email.equals(user.email) : user.email != null) &&
                !(this.getId() != null ? !this.getId().equals(user.getId()) : user.getId() != null) &&
                !(password != null ? !password.equals(user.password) : user.password != null) &&
                !(username != null ? !username.equals(user.username) : user.username != null);

    }

    @Override
    public int hashCode() {
        int result = this.getId() != null ? this.getId().hashCode() : 0;
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + this.getId() +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
