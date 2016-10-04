package no.nsd.qddt.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;

/**
 * Interface for all service classes dealing with entity classes
 * annotated by {@link org.hibernate.envers.Audited}
 *
 * @author Stig Norland
 * @author Dag Østgulen Heradstveit
 */
public interface BaseServiceAudit<T,ID, N extends Number & Comparable<N>> {

    /**
     * Find the latest changed revision.
     * @param id of the entity
     * @return {@link org.springframework.data.history.Revision}
     */
    Revision<N, T> findLastChange(ID id);

    /**
     * Find the entity based on a revision number.
     * @param id of the entity
     * @param revision number of the entity
     * @return {@link org.springframework.data.history.Revision} at the given revision
     */
    Revision<N, T> findRevision(ID id, Integer revision);

    /**
     * Find all revisions and return in a pageable view
     * @param id of the entity
     * @param pageable from controller method
     * @return {@link org.springframework.data.domain.Page} of the entity
     */
    Page<Revision<N, T>> findRevisions(ID id, Pageable pageable);

//    /**
//     * Find all revisions and return in a pageable view
//     * @param id of the entity
//     * @param changeKinds
//     * @param pageable from controller method
//     * @return {@link org.springframework.data.domain.Page} of the entity
//     */
//    Page<Revision<N,T>> findRevisionByIdAndChangeKindNotIn(UUID id, Collection<AbstractEntityAudit.ChangeKind> changeKinds, Pageable pageable);

}
