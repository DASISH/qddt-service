package no.nsd.qddt.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import no.nsd.qddt.domain.user.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Dag Østgulen Heradstveit
 * @author Stig Norland
 */
public class UserPrincipal implements UserDetails {

    private final User user;

    public UserPrincipal(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        final List<GrantedAuthority> authorities =
            user.getAuthorities().stream().map( (authority) ->
            new SimpleGrantedAuthority( authority.getAuthority() )
            ).collect( Collectors.toList() );
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
     *    not implemented, always return TRUE
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /*
     *   not implemented, always return TRUE
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /*
     *  not implemented, always return TRUE
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
