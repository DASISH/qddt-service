package no.nsd.qddt.domain.response;

import no.nsd.qddt.domain.AbstractEntity;
import no.nsd.qddt.domain.Agentcy;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


/**
 *
 * CodeList A special form of maintainable that allows a single codelist to be maintained outside of a CodeListScheme.
 *
 * @author Dag Østgulen Heradstveit
 * @author Stig Norland
 */
@Audited
@Entity
@Table(name = "responseDomain")
public class ResponseDomain extends AbstractEntity implements Serializable {

    @ManyToOne
    @JoinColumn(name = "agentcy_id")
    private Agentcy agentcy;

    @ManyToOne
    @JoinColumn(name = "response_kind_id")
    private ResponseKind responseKind;

    @NotAudited
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "pk.responseDomain", cascade = CascadeType.ALL)
    private Set<ResponseDomainCode> responseDomainCodes = new HashSet<>();

    public Agentcy getAgentcy() {return agentcy;}

    public void setAgentcy(Agentcy agentcy) {this.agentcy = agentcy;}

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

        if (agentcy != null ? !agentcy.equals(that.agentcy) : that.agentcy != null) return false;
        if (responseDomainCodes != null ? !responseDomainCodes.equals(that.responseDomainCodes) : that.responseDomainCodes != null)
            return false;
        if (responseKind != null ? !responseKind.equals(that.responseKind) : that.responseKind != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (agentcy != null ? agentcy.hashCode() : 0);
        result = 31 * result + (responseKind != null ? responseKind.hashCode() : 0);
        result = 31 * result + (responseDomainCodes != null ? responseDomainCodes.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ResponseDomain{" +
                "agentcy=" + agentcy +
                ", responseKind=" + responseKind +
                ", responseDomainCodes=" + responseDomainCodes +
                super.toString() +
                '}';
    }
}
