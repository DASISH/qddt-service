package no.nsd.qddt.domain;

import javax.persistence.*;
import java.util.UUID;

/**
 * @author Stig Norland
 */

@Entity
@Table(name = "Attachment")
public class Attachment extends AbstractEntity {

    @Column(name = "guid", columnDefinition = "BINARY(16)")
    private UUID guid = UUID.randomUUID();

    private String name;

    private ChangeKind changeReason;

    private String changeComment;

    @ManyToOne
    @JoinColumn(name="module_id")
    private Module module;

    private String path;


    public UUID getGuid() {
        return guid;
    }

    public void setGuid(UUID guid) {
        this.guid = guid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ChangeKind getChangeReason() {
        return changeReason;
    }

    public void setChangeReason(ChangeKind changeReason) {
        this.changeReason = changeReason;
    }

    public String getChangeComment() {
        return changeComment;
    }

    public void setChangeComment(String changeComment) {
        this.changeComment = changeComment;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Attachment that = (Attachment) o;

        if (changeComment != null ? !changeComment.equals(that.changeComment) : that.changeComment != null)
            return false;
        if (changeReason != null ? !changeReason.equals(that.changeReason) : that.changeReason != null) return false;
        if (module != null ? !module.equals(that.module) : that.module != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (path != null ? !path.equals(that.path) : that.path != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (changeReason != null ? changeReason.hashCode() : 0);
        result = 31 * result + (changeComment != null ? changeComment.hashCode() : 0);
        result = 31 * result + (module != null ? module.hashCode() : 0);
        result = 31 * result + (path != null ? path.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Attachment{" +
                ", name='" + name + '\'' +
                ", changeReason=" + changeReason +
                ", changeComment='" + changeComment + '\'' +
                ", module=" + module +
                ", path='" + path + '\'' +
                '}';
    }
}
