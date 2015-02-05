package no.nsd.qddt.service;

import no.nsd.qddt.domain.Comment;
import no.nsd.qddt.exception.ResourceNotFoundException;
import no.nsd.qddt.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 * @author Stig Norland
 */
@Service("commentService")
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Comment findById(Long id) {
        return commentRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(id, Comment.class)
        );
    }

//    @Override
//    public Comment findById(UUID id) {
//        return null;
//    }

    @Override
    @Transactional(readOnly = true)
    public List<Comment> findAll() {
        return commentRepository.findAll();
    }


    @Override
    @Transactional(readOnly = true)
    public Page<Comment> findAll(Pageable pageable) {
        return commentRepository.findAll(pageable);
    }


    @Override
    @Transactional(readOnly = false)
    public Comment save(Comment instance) {

        instance.setCreated(LocalDateTime.now());
        return commentRepository.save(instance);
    }


    @Override
    @Transactional(readOnly = false)
    public void delete(Comment instance) {
        commentRepository.delete(instance);
    }

    @Override
    @Transactional(readOnly = true)
    public Revision<Integer, Comment> findLastChange(Long id) {
        return commentRepository.findLastChangeRevision(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Revision<Integer, Comment> findEntityAtRevision(Long id, Integer revision) {
        return commentRepository.findEntityAtRevision(id,revision);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Revision<Integer, Comment>> findAllRevisionsPageable(Long id, Pageable pageable) {
        return commentRepository.findRevisions(id,pageable);
    }


//    @Override
//    @Transactional(readOnly = true)
//    public Page<Comment> findSiblingsPageable(Long id, Pageable pageable) {
//        return null; //commentRepository.findCommentByParentOrderByIdAsc( commentRepository.findOne(id).get, pageable);
//    }



}
