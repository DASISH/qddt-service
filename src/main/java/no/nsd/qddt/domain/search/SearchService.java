package no.nsd.qddt.domain.search;

import java.util.UUID;

/**
 * @author Stig Norland
 */
public interface SearchService {


    /**
     * Return a path based on its ID.
     * @param id ID
     * @return Entity
     */
    QddtUrl findPath(UUID id);
}
