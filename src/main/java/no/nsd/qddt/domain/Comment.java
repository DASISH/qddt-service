package no.nsd.qddt.domain;

import org.hibernate.annotations.*;
import org.hibernate.annotations.Parameter;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
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

    @Column(name = "guid")
    @Type(type="pg-uuid")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2", parameters = {
            @org.hibernate.annotations.Parameter(name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy") })
    private UUID guid;

    @Column(name = "owner_guid")
            @Type(type="pg-uuid")
            @GeneratedValue(generator = "uuid")
            @GenericGenerator(name = "uuid", strategy = "uuid2", parameters = {
            @Parameter(name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy") })
    private UUID ownerGuid;

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


    public UUID getGuid() {
        return guid;
    }

    public void setGuid(UUID guid) {
        this.guid = guid;
    }

    public UUID getOwnerGuid() {
        return ownerGuid;
    }

    public void setOwnerGuid(UUID ownerGuid) {
        this.ownerGuid = ownerGuid;
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
        this.children.add(comment);
      //  comment.setParent(this);
    }


    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Long treeSize(){
        return getChildren() == null ? 1 : getChildren().stream().mapToLong(c->c.treeSize()).sum() + 1;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Comment)) return false;
        if (!super.equals(o)) return false;

        Comment comment1 = (Comment) o;

        if (getGuid() != null ? !getGuid().equals(comment1.getGuid()) : comment1.getGuid() != null) return false;
        if (getOwnerGuid() != null ? !getOwnerGuid().equals(comment1.getOwnerGuid()) : comment1.getOwnerGuid() != null)
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
        result = 31 * result + (getGuid() != null ? getGuid().hashCode() : 0);
        result = 31 * result + (getOwnerGuid() != null ? getOwnerGuid().hashCode() : 0);
        result = 31 * result + (getParent() != null ? getParent().hashCode() : 0);
        result = 31 * result + (getChildren() != null ? getChildren().hashCode() : 0);
        result = 31 * result + (getComment() != null ? getComment().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "guid=" + guid +
                ", ownerGuid=" + ownerGuid +
                ", children=" + children +
                ", comment='" + comment + '\'' +
                '}';
    }
}