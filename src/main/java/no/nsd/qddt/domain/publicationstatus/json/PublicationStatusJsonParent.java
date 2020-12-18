package no.nsd.qddt.domain.publicationstatus.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import no.nsd.qddt.domain.publicationstatus.PublicationStatus;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Stig Norland
 */
public class PublicationStatusJsonParent extends PublicationStatusJsonChild {


    private final List<PublicationStatusJsonChild> children;

    @JsonIgnore
    private final
    Integer childrenIdx;

    public PublicationStatusJsonParent(PublicationStatus publicationStatus){
        super(publicationStatus);
        childrenIdx = publicationStatus.getChildrenIdx();
        children = publicationStatus.getChildren().stream()
                .map(PublicationStatusJsonChild::new)
                .collect(Collectors.toList());
    }

    public Integer getChildrenIdx() {
        return childrenIdx;
    }

    public List<PublicationStatusJsonChild> getChildren() {
        return children;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PublicationStatusJsonParent)) return false;
        if (!super.equals(o)) return false;

        PublicationStatusJsonParent that = (PublicationStatusJsonParent) o;

        return children != null ? children.equals(that.children) : that.children == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (children != null ? children.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return String.format(
                "PublicationStatusJsonListView (id=%s, label=%s, published=%s,  children=%s)",
                super.id, super.label, super.published, this.children.size());
    }


}
