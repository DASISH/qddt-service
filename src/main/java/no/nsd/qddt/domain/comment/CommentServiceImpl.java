package no.nsd.qddt.domain.comment;

import no.nsd.qddt.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        return commentRepository.existsById(uuid);
    }

    @Override
    public Comment findOne(UUID uuid) {
        return commentRepository.findById(uuid).orElseThrow(
                () -> new ResourceNotFoundException(uuid, Comment.class)
        );
    }

    @Override
    @Transactional()
    public Comment save(Comment instance) {
        return commentRepository.save(instance);
    }

//    @Override
//    public List<Comment> save(List<Comment> instances) {
//        return commentRepository.saveAll(instances);
//    }

    @Override
    public void delete(UUID uuid) {
        commentRepository.deleteById(uuid);
    }

    @Override
    public void delete(List<Comment> instances) {
        commentRepository.deleteAll(instances);
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
        return commentRepository.findAllByOwnerIdAndIsHiddenOrderByModifiedAsc(ownerId,false, pageable);
    }

    @Override
    public Page<Comment> findAllByOwnerIdPublicPageable(UUID ownerId, Pageable pageable) {
        return commentRepository.findAllByOwnerIdAndIsHiddenAndIsPublicOrderByModifiedAsc(ownerId,false,true, pageable);
    }

    @Override
    public List<Comment> findAllByOwnerId(UUID ownerId) {
        return commentRepository.findAllByOwnerIdAndIsHiddenOrderByModifiedAsc(ownerId,false);
    }

    @Override
    public List<Comment> findAllByOwnerIdPublic(UUID ownerId) {
        return commentRepository.findAllByOwnerIdAndIsHiddenAndIsPublicOrderByModifiedAsc(ownerId,false,true);
    }

}
