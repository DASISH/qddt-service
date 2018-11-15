package no.nsd.qddt.domain.publicationstatus;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import no.nsd.qddt.domain.agency.Agency;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Stig Norland
 **/

@Entity
@Table(name = "PUBLICATION_STATUS", uniqueConstraints = {@UniqueConstraint(columnNames = {"agency_id","label"},name = "UNQ_PUBLICATION_STATUS")})
public class PublicationStatus {

    public enum Published {
        NOT_PUBLISHED,
        INTERNAL_PUBLICATION,
        EXTERNAL_PUBLICATION
    }


    @Id
    @Column(name = "id", updatable = false)
//    @GenericGenerator(name = "sequence-generator",strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator")
//    @SequenceGenerator(  )
    private Long id;

    @ManyToOne
    @JoinColumn(name = "agency_id",updatable = false)
    private Agency agency;

    @Enumerated(EnumType.STRING)
    private Published published;

    private String label;

    private String Description;


    @JsonBackReference(value = "parentRef")
    @ManyToOne()
    @JoinColumn(name = "publication_status_id",updatable = false,insertable = false)
    private PublicationStatus parent;

    @Column(name = "publication_status_idx", updatable = false,insertable = false)
    @JsonIgnore
    private Integer parentIdx;

    @OneToMany(fetch = FetchType.LAZY)
    @OrderColumn(name = "publication_status_idx")
    @JoinColumn(name = "publication_status_id")
    private List<PublicationStatus> children = new ArrayList<>();


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Agency getAgency() {
        return agency;
    }

    public void setAgency(Agency agency) {
        this.agency = agency;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }


    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    /**
     * @return the published
     */
    public Published getPublished() {
        return published;
    }

    /**
     * @param published the published to set
     */
    public void setPublished(Published published) {
        this.published = published;
    }

    public List<PublicationStatus> getChildren() {
        return children;
    }

    public void setChildren(List<PublicationStatus> children) {
        this.children = children;
    }

    public Integer getChildrenIdx() {
        return parentIdx;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PublicationStatus)) return false;

        PublicationStatus that = (PublicationStatus) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (agency != null ? !agency.equals(that.agency) : that.agency != null) return false;
        return label != null ? label.equals(that.label) : that.label == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (agency != null ? agency.hashCode() : 0);
        result = 31 * result + (label != null ? label.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PublicationStatus{" +
                "id=" + id +
                ", agency=" + agency.getName() +
                ", label='" + label + '\'' +
                '}';
    }
}
