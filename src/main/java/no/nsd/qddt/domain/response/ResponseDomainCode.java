package no.nsd.qddt.domain.response;

import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Dag Østgulen Heradstveit
 */
@Audited
@Entity
@Table(name = "responseDomain_code")
public class ResponseDomainCode implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "code_id")
    private Long codeId;

    @Column(name = "responsedomain_id")
    private Long responseDomainId;

    @Column(name = "rank")
    private String rank;

    public ResponseDomainCode() {
    }

    public ResponseDomainCode(Long codeId, Long responseDomainId, String rank) {
        this.codeId = codeId;
        this.responseDomainId = responseDomainId;
        this.rank = rank;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCodeId() {
        return codeId;
    }

    public void setCodeId(Long codeId) {
        this.codeId = codeId;
    }

    public Long getResponseDomainId() {
        return responseDomainId;
    }

    public void setResponseDomainId(Long responseDomainId) {
        this.responseDomainId = responseDomainId;
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

        ResponseDomainCode that = (ResponseDomainCode) o;

        if (codeId != null ? !codeId.equals(that.codeId) : that.codeId != null) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (rank != null ? !rank.equals(that.rank) : that.rank != null) return false;
        if (responseDomainId != null ? !responseDomainId.equals(that.responseDomainId) : that.responseDomainId != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (codeId != null ? codeId.hashCode() : 0);
        result = 31 * result + (responseDomainId != null ? responseDomainId.hashCode() : 0);
        result = 31 * result + (rank != null ? rank.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ResponseDomainCode{" +
                "id=" + id +
                ", codeId=" + codeId +
                ", responseDomainId=" + responseDomainId +
                ", rank='" + rank + '\'' +
                '}';
    }
}