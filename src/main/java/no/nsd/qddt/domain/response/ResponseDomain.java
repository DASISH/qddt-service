package no.nsd.qddt.domain.response;

import no.nsd.qddt.domain.AbstractEntity;
import no.nsd.qddt.domain.Agency;
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
    @JoinColumn(name = "agency_id")
    private Agency agency;

    @ManyToOne
    @JoinColumn(name = "response_kind_id")
    private ResponseKind responseKind;

    public Agency getAgency() {return agency;}

    public void setAgency(Agency agency) {this.agency = agency;}

    public ResponseKind getResponseKind() {
        return responseKind;
    }

    public void setResponseKind(ResponseKind responseKind) {
        this.responseKind = responseKind;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ResponseDomain that = (ResponseDomain) o;

        if (agency != null ? !agency.equals(that.agency) : that.agency != null) return false;
        if (responseKind != null ? !responseKind.equals(that.responseKind) : that.responseKind != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (agency != null ? agency.hashCode() : 0);
        result = 31 * result + (responseKind != null ? responseKind.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ResponseDomain{" +
                "agency=" + agency +
                ", responseKind=" + responseKind +
                '}';
    }
}
