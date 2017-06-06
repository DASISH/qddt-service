package no.nsd.qddt.domain.publicationstatus.json;

import no.nsd.qddt.domain.publicationstatus.PublicationStatus;

import java.io.Serializable;

/**
 * @author Stig Norland
 */
public class PublicationStatusJsonChild implements Serializable {

    Long id;
    String label;
    String name;
    String description;

    public PublicationStatusJsonChild(PublicationStatus publicationStatus) {
        id = publicationStatus.getId();
        label = publicationStatus.getLabel();
        name = publicationStatus.getName();
        description = publicationStatus.getDescription();
    }

    public Long getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PublicationStatusJsonChild)) return false;

        PublicationStatusJsonChild that = (PublicationStatusJsonChild) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (label != null ? !label.equals(that.label) : that.label != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return description != null ? description.equals(that.description) : that.description == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (label != null ? label.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return String.format(
                "PublicationStatusJsonChild (id=%s, label=%s, name=%s, description=%s)", this.id, this.label, this.name, this.description);
    }


}
