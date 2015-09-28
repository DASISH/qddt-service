package no.nsd.qddt.service;

import no.nsd.qddt.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
public interface CommentService extends BaseServiceAudit<Comment,UUID> {

    Page<Comment> findAllByOwnerGuidPageable(UUID guid, Pageable pageable);

//    /**
//     * Find the latest changed revision.
//     * @param id of the entity
//     * @return {@link org.springframework.data.history.Revision}
//     */
//    Revision<Integer, Comment> findLastChange(Long id);
//
//    /**
//     * Find the entity based on a revision number.
//     * @param id of the entity
//     * @param revision number of the entity
//     * @return {@link org.springframework.data.history.Revision} at the given revision
//     */
//    Revision<Integer, Comment> findEntityAtRevision(Long id, Integer revision);
//
//    /**
//     * Find all revisions and return in a pageable view
//     * @param id of the entity
//     * @param pageable from controller method
//     * @return {@link org.springframework.data.domain.Page} of the entity
//     */
//    Page<Revision<Integer, Comment>> findAllRevisionsPageable(Long id, Pageable pageable);
//

}
