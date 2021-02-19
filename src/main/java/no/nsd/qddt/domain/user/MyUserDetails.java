package no.nsd.qddt.domain.user;

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
