package no.nsd.qddt.domain.comment;

import no.nsd.qddt.domain.user.User;
import no.nsd.qddt.domain.classes.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
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
    public boolean exists(UUID id) {
        return commentRepository.existsById(id);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR','ROLE_CONCEPT')")
    public Comment findOne(UUID id) {
        return commentRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(id, Comment.class)
        );
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR','ROLE_CONCEPT') and hasPermission(#instance,'OWNER')")
    public Comment save(Comment instance) {
        Comment retval = commentRepository.saveAndFlush(instance);
        commentRepository.indexChildren( retval.getOwnerId() );
        return retval;
    }


    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR','ROLE_CONCEPT')")
    public void delete(UUID id) {
        commentRepository.deleteById(id);
        commentRepository.indexChildren(id);
    }

    protected Comment prePersistProcessing(Comment instance) {
        return instance;
    }

    protected Comment postLoadProcessing(Comment instance) {
        return instance;
    }

    public Page<Comment> findAllByOwnerIdPageable(UUID ownerId, boolean showAll,  Pageable pageable) {
        assert  pageable != null;
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getDetails();
        assert user != null;
        return commentRepository.findByQueryPageable( ownerId, user.getId(), showAll, pageable);
    }

    public List<Comment> findAllByOwnerId(UUID ownerId, boolean showAll) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getDetails();
        assert user != null;
        return commentRepository.findByQuery( ownerId, user.getId(), showAll);
    }

}
