package no.nsd.qddt.domain.comment;

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
 * Today this relationship is like this ( Survey <- comment <- comment child )
 *
 * If we need to change this, we'll have to add a empty root comment for every survey and replace survey_id with this root element,
 * and add a reference for this root element to the corresponding survey , the relationship will be like this ( entity(survey) -> comment root <- comments)
 *
 * @author Dag Østgulen Heradstveit
 */

@Audited
@Entity
@Table(name = "comment")
public class Comment extends AbstractEntity implements Commentable {

    @Column(name = "owner_uuid")
    @Type(type="pg-uuid")
    private UUID ownerId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="parent_id")
    private Comment parent;

    @OneToMany(mappedBy="parent", cascade = CascadeType.ALL)
    private Set<Comment> children = new HashSet<>();


    @Column(name = "comment")
    public String comment;


    public Comment() {
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

    public Comment getParent() {
        return parent;
    }

    public void setParent(Comment parent) {
        this.parent = parent;
    }

    public Set<Comment> getChildren() {
        return children;
    }

    public void setChildren(Set<Comment> children) {
        this.children = children;
    }

    @Override
    public void addComment(Comment comment) {
        comment.setOwnerId(this.getOwnerId());
        comment.setParent(this);
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

    public Long treeSize(){
        return getChildren() == null ? 1 : getChildren().stream().mapToLong(Comment::treeSize).sum() + 1;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Comment)) return false;
        if (!super.equals(o)) return false;

        Comment comment1 = (Comment) o;

        if (getOwnerId() != null ? !getOwnerId().equals(comment1.getOwnerId()) : comment1.getOwnerId() != null)
            return false;
        if (getParent() != null ? !getParent().equals(comment1.getParent()) : comment1.getParent() != null)
            return false;
        if (getChildren() != null ? !getChildren().equals(comment1.getChildren()) : comment1.getChildren() != null)
            return false;
        return !(getComment() != null ? !getComment().equals(comment1.getComment()) : comment1.getComment() != null);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getOwnerId() != null ? getOwnerId().hashCode() : 0);
        result = 31 * result + (getParent() != null ? getParent().hashCode() : 0);
        result = 31 * result + (getChildren() != null ? getChildren().hashCode() : 0);
        result = 31 * result + (getComment() != null ? getComment().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Comment{" +
                ", ownerId=" + ownerId +
                ", children=" + children +
                ", comment='" + comment + '\'' +
                '}';
    }
}