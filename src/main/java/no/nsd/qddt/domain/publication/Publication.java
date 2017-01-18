package no.nsd.qddt.domain.publication;

import no.nsd.qddt.domain.AbstractEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Stig Norland
 */
@Entity
@Table(name = "PUBLICATION")
public class Publication extends AbstractEntity {

    String name;

    PublicationKind publicationKind;

    @OrderColumn(name="element_idx")
    @OrderBy("element_idx ASC")
    @ElementCollection
    @CollectionTable(name = "PUBLICATION_ELEMENT",joinColumns = @JoinColumn(name="element_id"))
    private List<PublicationElement>  publicationElements = new ArrayList<>();


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PublicationKind getPublicationKind() {
        return publicationKind;
    }

    public void setPublicationKind(PublicationKind selectableType) {
        this.publicationKind = selectableType;
    }

    public List<PublicationElement> getPublicationElements() {
        return publicationElements;
    }

    public void setPublicationElements(List<PublicationElement> publicationElements) {
        this.publicationElements = publicationElements;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Publication)) return false;
        if (!super.equals(o)) return false;

        Publication that = (Publication) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return (publicationKind == that.publicationKind);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (publicationKind != null ? publicationKind.hashCode() : 0);
        return result;
    }
}
