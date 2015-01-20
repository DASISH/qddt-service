package no.nsd.qddt.domain.response;

import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


/**
 * @author Stig Norland
 * @author Dag Østgulen Heradstveit
 */
//@Audited
@Entity
@Table(name = "responseDomain")
public class ResponseDomain {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "respons_kind_id")
    private ResponseKind responseKind;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "pk.responseDomain",
            cascade = CascadeType.ALL)
    private Set<ResponseDomainCode> responseDomainCodes = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ResponseKind getResponseKind() {
        return responseKind;
    }

    public void setResponseKind(ResponseKind responseKind) {
        this.responseKind = responseKind;
    }

    public Set<ResponseDomainCode> getResponseDomainCodes() {
        return responseDomainCodes;
    }

    public void setResponseDomainCodes(Set<ResponseDomainCode> responseDomainCodes) {
        this.responseDomainCodes = responseDomainCodes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ResponseDomain that = (ResponseDomain) o;

        if (responseKind != null ? !responseKind.equals(that.responseKind) : that.responseKind != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (responseKind != null ? responseKind.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ResponsDomain{" +
                "responsKind=" + responseKind +
                '}';
    }
}
