package no.nsd.qddt.security.user;

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

//    public void setOldPassword(String oldPassword) {
//        this.oldPassword = oldPassword;
//    }

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

        if (id != null ? !id.equals( password1.id ) : password1.id != null) return false;
        if (oldPassword != null ? !oldPassword.equals( password1.oldPassword ) : password1.oldPassword != null)
            return false;
        return password != null ? password.equals( password1.password ) : password1.password == null;
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
