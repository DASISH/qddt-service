package no.nsd.qddt.service;

import no.nsd.qddt.domain.User;
/**
 * @author Dag Østgulen Heradstveit
 * @author Stig Norland
 */

public interface UserService extends BaseService<User> {

    /**
     * Return a {@link no.nsd.qddt.domain.User} by email
     * @param email users email
     * @return the user belonging to the email
     */
    public User findByEmail(String email);

}
