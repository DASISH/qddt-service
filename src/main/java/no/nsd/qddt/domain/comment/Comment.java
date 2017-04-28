package no.nsd.qddt.domain.comment;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import no.nsd.qddt.domain.AbstractEntity;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.IOException;
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
 * @author Stig Norland
 */

//@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@Entity
@Table(name = "comment")
public class Comment extends AbstractEntity  {

    @Column(name = "owner_id", updatable = false)
    @Type(type="pg-uuid")
    private UUID ownerId;

    @OneToMany(mappedBy="ownerId", cascade = CascadeType.ALL, fetch = FetchType.EAGER,orphanRemoval = true)
    private Set<Comment> comments = new HashSet<>();

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


    public Set<Comment> getComments() {
        return this.comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
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
        return (comments == null) ? 0 : comments.stream().mapToInt(c-> c.getTreeSize() + 1).sum();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Comment)) return false;
        if (!super.equals(o)) return false;

        Comment comment1 = (Comment) o;

        if (ownerId != null ? !ownerId.equals(comment1.ownerId) : comment1.ownerId != null) return false;
        return !(comment != null ? !comment.equals(comment1.comment) : comment1.comment != null);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (ownerId != null ? ownerId.hashCode() : 0);
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


    public void fillDoc(Document document) throws IOException {
        document.setLeftMargin(document.getLeftMargin()+5f);
        document.add(new Paragraph(this.getModifiedBy().toString() + " - " + this.getModified()));
        document.add(new Paragraph(this.getComment()));

        for (Comment item : this.getComments()) {
            item.fillDoc(document);
        }

        document.setLeftMargin(document.getLeftMargin()-5f);
    }
}