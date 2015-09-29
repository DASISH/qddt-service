package no.nsd.qddt.domain.response;

import no.nsd.qddt.domain.AbstractEntityAudit;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;


import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

/**
 *
 * @author Dag Østgulen Heradstveit
 */
@Audited
@Entity
@Table(name = "responseDomain_code")
public class ResponseDomainCode extends AbstractEntityAudit {

    @Column(name = "rank")
    private int rank;

    @ManyToOne
    @JoinColumn(name = "responsedomain_id")
    private ResponseDomain responseDomain;

    @ManyToOne
    @JoinColumn(name = "code_id")
    private Code code;

    public ResponseDomainCode() {
    }

    public ResponseDomainCode(int rank, ResponseDomain responseDomain, Code code) {
        this.rank = rank;
        this.responseDomain = responseDomain;
        this.code = code;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public ResponseDomain getResponseDomain() {
        return responseDomain;
    }

    public void setResponseDomain(ResponseDomain responseDomain) {
        this.responseDomain = responseDomain;
    }

    public Code getCode() {
        return code;
    }

    public void setCode(Code code) {
        this.code = code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ResponseDomainCode that = (ResponseDomainCode) o;

        if (rank != that.rank) return false;
        if (code != null ? !code.equals(that.code) : that.code != null) return false;
        if (responseDomain != null ? !responseDomain.equals(that.responseDomain) : that.responseDomain != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = responseDomain != null ? responseDomain.hashCode() : 0;
        result = 31 * result + rank;
        result = 31 * result + (code != null ? code.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ResponseDomainCode{" +
                ", rank=" + rank +
                ", responseDomain=" + responseDomain +
                ", code=" + code +
                '}';
    }
}