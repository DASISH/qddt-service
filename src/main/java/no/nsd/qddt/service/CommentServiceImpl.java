package no.nsd.qddt.service;

import no.nsd.qddt.domain.Comment;
import no.nsd.qddt.exception.ResourceNotFoundException;
import no.nsd.qddt.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

    @Override
    public List<Comment> findAll() {
        return commentRepository.findAll();
    }

    @Override
    public Page<Comment> findAllPageable(Pageable pageable) {
        return commentRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = false)
    public Comment save(Comment instance) {

        instance.setCreated(LocalDateTime.now());
        return commentRepository.save(instance);
    }

    @Override
    public Page<Comment> findSiblingsPageable(Long id, Pageable pageable) {
        return commentRepository.findCommentByParentOrderByIdAsc(
                commentRepository.findOne(id), pageable);
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(Comment instance) {
        commentRepository.delete(instance);
    }


}
