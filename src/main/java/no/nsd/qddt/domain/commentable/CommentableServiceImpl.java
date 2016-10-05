package no.nsd.qddt.domain.commentable;

import no.nsd.qddt.domain.AbstractEntity;
import no.nsd.qddt.domain.comment.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

/**
 * @author Dag Østgulen Heradstveit.
 */
@Service("commentableService")
public class CommentableServiceImpl implements CommentableService {

    private CommentableRepository commentableRepository;

    @Autowired
    public CommentableServiceImpl(CommentableRepository commentableRepository) {
        this.commentableRepository = commentableRepository;
    }

    @Override
    @Transactional(readOnly = false)
    public Comment saveComment(Commentable commentable, Comment comment) {
        if(commentable instanceof AbstractEntity)
            comment.setOwnerId(((AbstractEntity) commentable).getId());
            return commentableRepository.save(comment);
    }

    @Override
    public List<Comment> populate(Commentable commentable) {
        if(commentable instanceof AbstractEntity)
            return commentableRepository.findbyOwnerIdAndisHidden(
                    ((AbstractEntity)commentable).getId(),false);

        else return Collections.emptyList();
    }
}