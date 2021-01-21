package no.nsd.qddt.security.userdetails;

import no.nsd.qddt.domain.user.User;
import org.springframework.security.core.userdetails.UserDetails;

public class MyUserDetails extends User implements UserDetails {

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }
}
