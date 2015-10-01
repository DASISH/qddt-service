package no.nsd.qddt.domain.comment;

import no.nsd.qddt.domain.AbstractEntity;
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
public class Comment extends AbstractEntity {

    @Column(name = "owner_uuid")
    @Type(type="pg-uuid")
    private UUID ownerUUID;

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

    public UUID getOwnerUUID() {
        return ownerUUID;
    }

    public void setOwnerUUID(UUID ownerUUID) {
        this.ownerUUID = ownerUUID;
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

    /**
     * Add a new comment to the set.
     * @param comment to be added to parent.
     */
    public void addComment(Comment comment) {
        comment.setOwnerUUID(this.getOwnerUUID());
        comment.setParent(this);
        //this.children.add(comment);
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

        if (getOwnerUUID() != null ? !getOwnerUUID().equals(comment1.getOwnerUUID()) : comment1.getOwnerUUID() != null)
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
        result = 31 * result + (getOwnerUUID() != null ? getOwnerUUID().hashCode() : 0);
        result = 31 * result + (getParent() != null ? getParent().hashCode() : 0);
        result = 31 * result + (getChildren() != null ? getChildren().hashCode() : 0);
        result = 31 * result + (getComment() != null ? getComment().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Comment{" +
                ", ownerUUID=" + ownerUUID +
                ", children=" + children +
                ", comment='" + comment + '\'' +
                '}';
    }
}