package no.nsd.qddt.domain.comment;

import no.nsd.qddt.domain.classes.interfaces.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
public interface CommentService extends BaseService<Comment,UUID> {

    Page<Comment> findAllByOwnerIdPageable(UUID ownerId, boolean showAll,  Pageable pageable);

    List<Comment> findAllByOwnerId(UUID ownerId, boolean showAll);




}
