package no.nsd.qddt.domain.comment;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import no.nsd.qddt.domain.AbstractEntity;
import no.nsd.qddt.domain.commentable.Commentable;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Current demand for comments is just for Survey. If we want to extend this to other entities we must change
 * the relationship and hold the entry comment at each entity that wants a comment tree. Today we hold this in comments themselves.
 * Today this relationship is like this ( Survey &#8592;  comment &#8592;  comment child )
 *
 * If we need to change this, we'll have to add a empty root comment for every survey and replace survey_id with this root element,
 * and add a reference for this root element to the corresponding survey , the relationship will be like this ( entity(survey) &#8594;  comment root &#8592;  comments)
 *
 * @author Dag Østgulen Heradstveit
 */

@Audited
@Entity
@Table(name = "comment")
public class Comment extends AbstractEntity implements Commentable {

    @Column(name = "owner_uuid", updatable = false)
    @Type(type="pg-uuid")
    private UUID ownerId;

    @OneToMany(mappedBy="ownerId", cascade = CascadeType.ALL, fetch = FetchType.EAGER,orphanRemoval = true)
    private Set<Comment> children = new HashSet<>();

    private boolean isHidden;

    @Column(name = "comment",length = 2000)
    public String comment;

    public Comment() {
        isHidden = false;
    }

    public Comment(String comment) {
        setComment(comment);
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(UUID ownerId) {
        this.ownerId = ownerId;
    }

    public Set<Comment> getChildren() {
        return children;
    }

    public void setChildren(Set<Comment> children) {
        this.children = children;
    }

    @Override
    public void addComment(Comment comment) {
        this.children.add(comment);
        comment.setOwnerId(this.getOwnerId());
    }

    @Override
    public Set<Comment> getComments() {
        return this.children;
    }

    @Override
    public void setComments(Set<Comment> comments) {
        this.children = comments;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean getIsHidden() {
        return isHidden;
    }

    public void setIsHidden(boolean hidden) {
        isHidden = hidden;
    }

    @Transient
    @JsonSerialize()
    public int getTreeSize(){
        return getChildren() == null ? 1 : getChildren().stream().mapToInt(Comment::getTreeSize).sum() + 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Comment)) return false;
        if (!super.equals(o)) return false;

        Comment comment1 = (Comment) o;

        if (ownerId != null ? !ownerId.equals(comment1.ownerId) : comment1.ownerId != null) return false;
//        if (parent != null ? !parent.equals(comment1.parent) : comment1.parent != null) return false;
        return !(comment != null ? !comment.equals(comment1.comment) : comment1.comment != null);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (ownerId != null ? ownerId.hashCode() : 0);
//        result = 31 * result + (parent != null ? parent.hashCode() : 0);
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "ownerId=" + ownerId +
                ", comment='" + comment + '\'' +
                "} " + super.toString();
    }

    public void removeChildren() {
        getChildren().forEach(C ->{
            C.removeChildren();
            C.setChildren(null);
        });
    }
}