package no.nsd.qddt.security.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 * @author Stig Norland
 */
public class QDDTUserDetails implements UserDetails {

    private static final long serialVersionUID = -2985656388091941799L;

    private final User user;
    private final List<? extends GrantedAuthority> authorities;

    public QDDTUserDetails(User user, List<? extends GrantedAuthority> authorities) {
        this.user = user;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public UUID getId() {
        return user.getId();
    }

    public User getUser() {
        return user;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    /*
    not implemented, always return TRUE
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /*
    not implemented, always return TRUE
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /*
    not implemented, always return TRUE
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return user.isEnabled();
    }
}
