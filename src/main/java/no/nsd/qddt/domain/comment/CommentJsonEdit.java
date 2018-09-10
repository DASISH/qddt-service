package no.nsd.qddt.domain.comment;

import no.nsd.qddt.domain.user.json.UserJson;
import org.hibernate.annotations.Type;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Stig Norland
 */
public class CommentJsonEdit {

    @Type(type="pg-uuid")
    private UUID id;

    private boolean isPublic;

    private String comment;

    private Set<CommentJsonEdit> comments = new HashSet<>();

    private Timestamp modified;

    private UserJson modifiedBy;

    public CommentJsonEdit() {
    }

    public CommentJsonEdit(Comment comment) {
        setId(comment.getId());
        setPublic(comment.isPublic());
        setComment(comment.getComment());
        setComments(comment.getComments().stream().map(CommentJsonEdit::new).collect(Collectors.toSet()));
        setModified(comment.getModified());
        setModifiedBy(comment.getModifiedBy());
    }


    public UUID getId() {
        return id;
    }

    private void setId(UUID id) {
        this.id = id;
    }

    public Timestamp getModified() {
        return modified;
    }

    private void setModified(Timestamp modified) {
        this.modified = modified;
    }

    public UserJson getModifiedBy() {
        return modifiedBy;
    }

    private void setModifiedBy(UserJson modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Set<CommentJsonEdit> getComments() {
        return comments;
    }

    private void setComments(Set<CommentJsonEdit> comments) {
        this.comments = comments;
    }

    public String getComment() {
        return comment;
    }

    private void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }
}
