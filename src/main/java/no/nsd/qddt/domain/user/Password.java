package no.nsd.qddt.domain.user;

import java.util.Objects;
import java.util.UUID;

/**
 * @author Stig Norland
 */
public class Password {

    UUID id;

    String oldPassword;

    String password;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Password)) return false;

        Password password1 = (Password) o;

        if (!Objects.equals( id, password1.id )) return false;
        if (!Objects.equals( oldPassword, password1.oldPassword ))
            return false;
        return Objects.equals( password, password1.password );
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (oldPassword != null ? oldPassword.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "{\"Password\":{"
            + "\"id\":" + id
            + ", \"oldPassword\":\"" + oldPassword.hashCode() + "\""
            + ", \"password\":\"" + password.hashCode() + "\""
            + "}}";
    }


}
