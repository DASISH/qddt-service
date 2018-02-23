package no.nsd.qddt.domain.comment;

import no.nsd.qddt.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 * @author Stig Norland
 */
@Service("commentService")
class CommentServiceImpl  implements CommentService  {

    private final CommentRepository commentRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }


    @Override
    public long count() {
        return commentRepository.count();
    }

    @Override
    public boolean exists(UUID uuid) {
        return commentRepository.exists(uuid);
    }

    @Override
    public Comment findOne(UUID uuid) {
        return commentRepository.findById(uuid).orElseThrow(
                () -> new ResourceNotFoundException(uuid, Comment.class)
        );
    }

    @Override
    @Transactional()
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_SUPER','ROLE_USER')")
    public Comment save(Comment instance) {
        return commentRepository.save(instance);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_SUPER','ROLE_USER')")
    public List<Comment> save(List<Comment> instances) {
        return commentRepository.save(instances);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_SUPER')")
    public void delete(UUID uuid) {
        commentRepository.delete(uuid);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_SUPER')")
    public void delete(List<Comment> instances) {
        commentRepository.delete(instances);
    }


    protected Comment prePersistProcessing(Comment instance) {
        return instance;
    }


    protected Comment postLoadProcessing(Comment instance) {
        return instance;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Comment> findAllByOwnerIdPageable(UUID ownerId, Pageable pageable) {
        return commentRepository.findAllByOwnerIdAndHiddenOrderByModifiedAsc(ownerId,false, pageable);
    }

    @Override
    public Page<Comment> findAllByOwnerIdPublicPageable(UUID ownerId, Pageable pageable) {
        return commentRepository.findAllByOwnerIdAndHiddenAndPublicOrderByModifiedAsc(ownerId,false,true, pageable);
    }

    @Override
    public List<Comment> findAllByOwnerId(UUID ownerId) {
        return commentRepository.findAllByOwnerIdAndHiddenOrderByModifiedAsc(ownerId,false);
    }

    @Override
    public List<Comment> findAllByOwnerIdPublic(UUID ownerId) {
        return commentRepository.findAllByOwnerIdAndHiddenAndPublicOrderByModifiedAsc(ownerId,false,true);
    }

}
