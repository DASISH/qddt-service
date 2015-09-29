package no.nsd.qddt.repository;

import no.nsd.qddt.domain.Comment;
import no.nsd.qddt.domain.OtherMaterial;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.envers.repository.support.EnversRevisionRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
@Repository
public interface CommentRepository extends BaseRepository<Comment,UUID>, EnversRevisionRepository<Comment, UUID, Integer> {

    /**
     * @param ownerUUID Is a parentUUID.
     * @param pageable Pageable object
     * @return All attachments that belongs to the module with moduleId.
     */

    Page<Comment> findAllByOwnerUUIDOrderByCreatedDesc(UUID ownerUUID, Pageable pageable);

}
