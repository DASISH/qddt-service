package no.nsd.qddt.domain.comment;

import no.nsd.qddt.domain.user.UserJson;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;
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

    private LocalDateTime modified;

    private UserJson modifiedBy;

    @Type(type="pg-uuid")
    private UUID ownerId;

    private Set<CommentJsonEdit> comments = new HashSet<>();

    private boolean isHidden;

    private boolean isPublic;

    private String comment;

    public CommentJsonEdit() {
    }

    public CommentJsonEdit(Comment comment) {
        setId(comment.getId());
        setModified(comment.getModified());
        setModifiedBy(new UserJson(comment.getModifiedBy()));
        setOwnerId(comment.getOwnerId());
        setComments(comment.getComments().stream().map(CommentJsonEdit::new).collect(Collectors.toSet()));
        setIsHidden(comment.getIsHidden());
        setPublic(comment.isPublic());
        setComment(comment.getComment());
    }


    public UUID getId() {
        return id;
    }

    private void setId(UUID id) {
        this.id = id;
    }

    public LocalDateTime getModified() {
        return modified;
    }

    private void setModified(LocalDateTime modified) {
        this.modified = modified;
    }

    public UserJson getModifiedBy() {
        return modifiedBy;
    }

    private void setModifiedBy(UserJson modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    private void setOwnerId(UUID ownerId) {
        this.ownerId = ownerId;
    }

    public Set<CommentJsonEdit> getComments() {
        return comments;
    }

    private void setComments(Set<CommentJsonEdit> comments) {
        this.comments = comments;
    }

    public boolean getIsHidden() {
        return isHidden;
    }

    private void setIsHidden(boolean hidden) {
        isHidden = hidden;
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
