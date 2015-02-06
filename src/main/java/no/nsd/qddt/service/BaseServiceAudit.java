package no.nsd.qddt.service;

import org.apache.commons.lang.NotImplementedException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;

import java.util.UUID;

/**
 * @author Stig Norland
 * @author Dag Østgulen Heradstveit
 */
public interface BaseServiceAudit<T> extends BaseService<T> {

    /**
     * Return a entity based on its UUID.
     * @param id UUID
     * @return T
     */
    default public T findByGuid(UUID id) {
        throw new NotImplementedException();
    }

    /**
     * Find the latest changed revision.
     * @param id of the entity
     * @return {@link org.springframework.data.history.Revision}
     */
    Revision<Integer, T> findLastChange(Long id);

    /**
     * Find the entity based on a revision number.
     * @param id of the entity
     * @param revision number of the entity
     * @return {@link org.springframework.data.history.Revision} at the given revision
     */
    Revision<Integer, T> findEntityAtRevision(Long id, Integer revision);

    /**
     * Find all revisions and return in a pageable view
     * @param id of the entity
     * @param pageable from controller method
     * @return {@link org.springframework.data.domain.Page} of the entity
     */
    Page<Revision<Integer, T>> findAllRevisionsPageable(Long id, Pageable pageable);

}