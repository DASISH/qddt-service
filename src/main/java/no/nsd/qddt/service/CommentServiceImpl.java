package no.nsd.qddt.service;

import no.nsd.qddt.domain.Comment;
import no.nsd.qddt.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * @author Dag Østgulen Heradstveit
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
        return commentRepository.findOne(id);
    }

    @Override
    @Transactional(readOnly = false)
    public Comment save(Comment comment) {
        comment.setCreated(LocalDateTime.now());
        return commentRepository.save(comment);
    }

    @Override
    public Page<Comment> findThreadByIdPageable(Long id, Pageable pageable) {
        return commentRepository.findCommentByParentIdSortByRankAscPageable(id,pageable);
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(Comment comment) {
        commentRepository.delete(comment);
    }

    @Override
    @Transactional(readOnly = true)
    public Revision<Integer, Comment> findLastChange(Long id) {
        return commentRepository.findLastChangeRevision(id);
    }

}
