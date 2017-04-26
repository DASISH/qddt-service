package no.nsd.qddt.domain.publicationstatus;

import com.fasterxml.jackson.annotation.JsonView;
import no.nsd.qddt.domain.agency.Agency;
import no.nsd.qddt.jsonviews.View;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Stig Norland
 */
@Entity
@Table(name = "PUBLICATION_STATUS", uniqueConstraints = {@UniqueConstraint(columnNames = {"agency_id","status"},name = "UNQ_PUBLICATION_STATUS")})
public class PublicationStatus {

    @Id
    @Column(name = "id")
    @GeneratedValue()
    private Long id;

    @ManyToOne
    @JoinColumn(name = "agency_id",updatable = false)
    private Agency agency;

    @JsonView(View.Simple.class)
    private String status;

    @JsonView(View.SimpleList.class)
    @OrderColumn(name = "children_idx")
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "parent_id")
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<PublicationStatus> getChildren() {
        return children;
    }

    public void setChildren(List<PublicationStatus> children) {
        this.children = children;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PublicationStatus)) return false;

        PublicationStatus that = (PublicationStatus) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (agency != null ? !agency.equals(that.agency) : that.agency != null) return false;
        return status != null ? status.equals(that.status) : that.status == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (agency != null ? agency.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PublicationStatus{" +
                "id=" + id +
                ", agency=" + agency.getName() +
                ", status='" + status + '\'' +
                '}';
    }
}
