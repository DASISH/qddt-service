package no.nsd.qddt.domain.user;

import java.util.UUID;

/**
 * @author Stig Norland
 */
public interface IPassword {

    UUID getId();

    String getOldPassword();

    String getNewPassword();

}
