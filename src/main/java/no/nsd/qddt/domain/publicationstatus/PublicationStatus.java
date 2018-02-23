package no.nsd.qddt.domain.publicationstatus;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import no.nsd.qddt.domain.agency.Agency;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Stig Norland
 */
@Entity
@Table(name = "PUBLICATION_STATUS", uniqueConstraints = {@UniqueConstraint(columnNames = {"agency_id","label"},name = "UNQ_PUBLICATION_STATUS")})
public class PublicationStatus {

    private Long id;
    private String name;
    private String label;
    private String Description;
    private Agency agency;

    private PublicationStatus parent;
    private Integer childrenIdx;
    private List<PublicationStatus> children = new ArrayList<>();



    @Id
    @Column(name = "id")
    @GeneratedValue()
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }


    public String getLabel() {
        return label;
    }
    public void setLabel(String label) {
        this.label = label;
    }


    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }


    public String getDescription() {
        return Description;
    }
    public void setDescription(String description) {
        Description = description;
    }

    @JsonBackReference(value = "parentRef")
    @ManyToOne()
    @JoinColumn(name = "parent_id",updatable = false,insertable = false)
    public PublicationStatus getParent() {
        return parent;
    }
    public void setParent(PublicationStatus parent) {
        this.parent = parent;
    }

    @ManyToOne
    @JoinColumn(name = "agency_id",updatable = false)
    public Agency getAgency() {
        return agency;
    }
    public void setAgency(Agency agency) {
        this.agency = agency;
    }


    @OneToMany(fetch = FetchType.EAGER)
    @OrderColumn(name = "children_idx")
    @JoinColumn(name = "parent_id")
    public List<PublicationStatus> getChildren() {
        return children;
    }
    public void setChildren(List<PublicationStatus> children) {
        this.children = children;
    }

    @Column(name = "children_idx", updatable = false,insertable = false)
    @JsonIgnore
    public Integer getChildrenIdx() {
        return childrenIdx;
    }
    public void setChildrenIdx(Integer childrenIdx) {
        this.childrenIdx = childrenIdx;
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
