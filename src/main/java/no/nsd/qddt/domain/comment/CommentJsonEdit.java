package no.nsd.qddt.domain.comment;

import no.nsd.qddt.domain.user.json.UserJson;
import org.hibernate.annotations.Type;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Stig Norland
 */
public class CommentJsonEdit {

    @Type(type="pg-uuid")
    private UUID id;

    @Type(type="pg-uuid")
    private UUID ownerId;

    private boolean isPublic;

    private String comment;

    private List<CommentJsonEdit> comments = new ArrayList<>();

    private int size;

    private Timestamp modified;

    private UserJson modifiedBy;

    public CommentJsonEdit() {
    }

    public CommentJsonEdit(Comment comment) {
        setId(comment.getId());
        setOwnerId( comment.getOwnerId() );
        setPublic(comment.isPublic());
        setComment(comment.getComment());
        setModified(comment.getModified());
        setModifiedBy(comment.getModifiedBy());
        setComments(comment.getComments().stream().map(CommentJsonEdit::new)
//            .sorted( Comparator.comparing( Comment::getOwnerIdx))
            .collect(Collectors.toList()));
        this.size = comment.getSize();
    }


    public UUID getId() {
        return id;
    }

    private void setId(UUID id) {
        this.id = id;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(UUID ownerId) {
        this.ownerId = ownerId;
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

    public List<CommentJsonEdit> getComments() {
        return comments;
    }

    private void setComments(List<CommentJsonEdit> comments) {
        this.comments = comments;
    }

    public int getSize() {
        return this.size;
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
