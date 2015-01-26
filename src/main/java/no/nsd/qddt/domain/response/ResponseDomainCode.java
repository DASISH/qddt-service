package no.nsd.qddt.domain.response;

import no.nsd.qddt.domain.AbstractEntity;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Dag Østgulen Heradstveit
 */
@Audited
@Entity
@Table(name = "responseDomain_code")
public class ResponseDomainCode extends AbstractEntity implements Serializable {

    @Column(name = "code_id")
    private Long codeId;

    @Column(name = "responsedomain_id")
    private Long responseCodeId;

    @Column(name = "rank")
    private String rank;

    public ResponseDomainCode() {
    }

    public ResponseDomainCode(Long responseCodeId, String rank) {
        this.responseCodeId = responseCodeId;
        this.rank = rank;
    }

    public Long getCodeId() {
        return codeId;
    }

    public void setCodeId(Long codeId) {
        this.codeId = codeId;
    }

    public Long getResponseCodeId() {
        return responseCodeId;
    }

    public void setResponseCodeId(Long responseCodeId) {
        this.responseCodeId = responseCodeId;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ResponseDomainCode that = (ResponseDomainCode) o;

        if (codeId != null ? !codeId.equals(that.codeId) : that.codeId != null) return false;
        if (rank != null ? !rank.equals(that.rank) : that.rank != null) return false;
        if (responseCodeId != null ? !responseCodeId.equals(that.responseCodeId) : that.responseCodeId != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (codeId != null ? codeId.hashCode() : 0);
        result = 31 * result + (responseCodeId != null ? responseCodeId.hashCode() : 0);
        result = 31 * result + (rank != null ? rank.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ResponseDomainCode{" +
                "codeId=" + codeId +
                ", responseCodeId=" + responseCodeId +
                ", rank='" + rank + '\'' +
                '}';
    }
}