package no.nsd.qddt.domain.comment;

import java.util.Set;

/**
 * Interface defining functionality for all entity classes that support
 * the notion of a comment and define the required functionality to
 * support it.
 *
 * @author Dag Østgulen Heradstveit
 */
public interface Commentable {

    /**
     * Add a {@link Comment} to a {@link Set} of comments.
     * @param comment added comment.
     */
    void addComment(Comment comment);

    /**
     * Get all comments attached to this entity as a {@link Set}
     */
    Set<Comment> getComments();

    /**
     * Set the {@link Set} of {@link Comment} for the entity.
     * @param comments populated set of comments.
     */
    void setComments(Set<Comment> comments);

}
