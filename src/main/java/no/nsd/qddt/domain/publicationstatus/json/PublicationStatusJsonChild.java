package no.nsd.qddt.domain.publicationstatus.json;

import no.nsd.qddt.domain.publicationstatus.PublicationStatus;

import java.io.Serializable;

/**
 * @author Stig Norland
 */
class PublicationStatusJsonChild implements Serializable {

    final Long id;
    final String label;
    final PublicationStatus.Published published;
    private final String description;

    PublicationStatusJsonChild(PublicationStatus publicationStatus) {
        id = publicationStatus.getId();
        label = publicationStatus.getLabel();
        published = publicationStatus.getPublished();
        description = publicationStatus.getDescription();
    }

    public Long getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public PublicationStatus.Published getPublished() {
        return published;
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
        if (published != null ? !published.equals(that.published) : that.published != null) return false;
        return description != null ? description.equals(that.description) : that.description == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (label != null ? label.hashCode() : 0);
        result = 31 * result + (published != null ? published.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return String.format(
                "PublicationStatusJsonChild (id=%s, label=%s, published=%s, description=%s)", this.id, this.label, this.published, this.description);
    }


}
