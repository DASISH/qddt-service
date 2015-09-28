package no.nsd.qddt.service;

import no.nsd.qddt.domain.User;

import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 * @author Stig Norland
 */

public interface UserService extends BaseService<User,UUID> {

    /**
     * Return a {@link no.nsd.qddt.domain.User} by email
     * @param email users email
     * @return the user belonging to the email
     */
    User findByEmail(String email);

}
