package no.nsd.qddt.domain.response;

import org.hibernate.envers.Audited;

import javax.persistence.*;

/**
 * @author Dag Østgulen Heradstveit
 */
//@Audited
@Entity
@Table(name = "responseDomain_code")
@AssociationOverrides(value = {
        @AssociationOverride(name = "pk.responseDomain",
                joinColumns = @JoinColumn(name = "responseDomain_id")),
        @AssociationOverride(name = "pk.code",
                joinColumns = @JoinColumn(name = "code_id"))
})
public class ResponseDomainCode {

    @EmbeddedId
    private ResponseDomainCodeId pk = new ResponseDomainCodeId();

    @Column(name = "rank")
    private String rank;

    public ResponseDomainCodeId getPk() {
        return pk;
    }

    public void setPk(ResponseDomainCodeId pk) {
        this.pk = pk;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public ResponseDomain getResponseDomain() {
        return getPk().getResponseDomain();
    }

    @Transient
    public void setResponseDomain(ResponseDomain responseDomain) {
        this.getPk().setResponseDomain(responseDomain);
    }

    public Code getCode() {
        return getPk().getCode();
    }

    @Transient
    public void setCode(Code code) {
        this.getPk().setCode(code);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ResponseDomainCode that = (ResponseDomainCode) o;

        if(getPk() != null ? !getPk().equals(that.getPk()): that.getPk() != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (getPk() != null ? getPk().hashCode() : 0);
    }

}
