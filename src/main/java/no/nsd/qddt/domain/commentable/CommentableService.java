package no.nsd.qddt.domain.commentable;

import no.nsd.qddt.domain.comment.Comment;

import java.util.List;

/**
 * @author Dag Østgulen Heradstveit.
 */
public interface  CommentableService {

    /**
     * Save a {@link Comment} to the correct {@link Commentable}
     * @param commentable Owner of the comment
     * @param comment The comment to save
     * @return The saved comment
     */
    Comment saveComment(Commentable commentable, Comment comment);

    /**
     * Populates a list with {@link Comment}
     * @param commentable Owner of the comments
     * @return A complete list of comments belonging to an owner
     */
    List<Comment> populate(Commentable commentable);
}
