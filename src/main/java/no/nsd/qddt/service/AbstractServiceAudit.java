package no.nsd.qddt.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;

import java.util.UUID;

/**
 * @author Stig Norland
 * @author Dag Østgulen Heradstveit
 */
public interface AbstractServiceAudit<T> extends AbstractService<T> {

    /**
     * Return a entity based on its UUID.
     * @param id UUID
     * @return T
     */
    public T findById(UUID id);

    /**
     * Find the latest changed revision.
     * @param id of the entity
     * @return {@link org.springframework.data.history.Revision}
     */
    public Revision<Integer, T> findLastChange(Long id);

    /**
     * Find the entity based on a revision number.
     * @param id of the entity
     * @param revision number of the entity
     * @return {@link org.springframework.data.history.Revision} at the given revision
     */
    public Revision<Integer, T> findEntityAtRevision(Long id, Integer revision);

    /**
     * Find all revisions and return in a pageable view
     * @param id of the entity
     * @param pageable from controller method
     * @return {@link org.springframework.data.domain.Page} of the entity
     */
    public Page<Revision<Integer, T>> findAllRevisionsPageable(Long id, Pageable pageable);

}
