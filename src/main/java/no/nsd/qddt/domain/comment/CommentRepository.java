package no.nsd.qddt.domain.comment;

import no.nsd.qddt.domain.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
@Repository
interface CommentRepository extends BaseRepository<Comment,UUID> {

    /**
     * @param ownerUUID Is a parentUUID.
     * @param pageable Pageable object
     * @return All attachments that belongs to the module with moduleId.
     */
    Page<Comment> findAllByOwnerIdOrderByModifiedAsc(UUID ownerUUID, Pageable pageable);

}
