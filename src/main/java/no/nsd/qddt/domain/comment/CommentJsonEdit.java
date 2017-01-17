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

    public String comment;

    public CommentJsonEdit() {
    }

    public CommentJsonEdit(Comment comment) {
        setId(comment.getId());
        setModified(comment.getModified());
        setModifiedBy(new UserJson(comment.getModifiedBy()));
        setOwnerId(comment.getOwnerId());
        setComments(comment.getComments().stream().map(F-> new CommentJsonEdit(F)).collect(Collectors.toSet()));
        setIsHidden(comment.getIsHidden());
        setComment(comment.getComment());
    }


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDateTime getModified() {
        return modified;
    }

    public void setModified(LocalDateTime modified) {
        this.modified = modified;
    }

    public UserJson getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(UserJson modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(UUID ownerId) {
        this.ownerId = ownerId;
    }

    public Set<CommentJsonEdit> getComments() {
        return comments;
    }

    public void setComments(Set<CommentJsonEdit> comments) {
        this.comments = comments;
    }

    public boolean getIsHidden() {
        return isHidden;
    }

    public void setIsHidden(boolean hidden) {
        isHidden = hidden;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
