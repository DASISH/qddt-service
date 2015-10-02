package no.nsd.qddt.domain.comment;

import java.util.List;

public abstract class CommentableService {



    List<Comment> findAllComments(Commentable commentable) {
        return null;
    }

    void saveComment(Commentable commentable, Comment comment) {

    }

    void findById(Commentable commentable,  Comment comment) {

    }
}
