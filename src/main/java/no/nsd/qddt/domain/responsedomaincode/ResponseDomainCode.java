package no.nsd.qddt.domain.responsedomaincode;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.code.Code;
import no.nsd.qddt.domain.responsedomain.ResponseDomain;
import org.hibernate.envers.Audited;

import javax.persistence.*;

/**
 *
 * @author Dag Østgulen Heradstveit
 */
@Audited
@Entity
@Table(name = "RESPONSEDOMAIN_CODE")
public class ResponseDomainCode extends AbstractEntityAudit {

    @ManyToOne
    @JoinColumn(name = "responsedomain_id")
    private ResponseDomain responseDomain;

    @ManyToOne
    @JoinColumn(name = "code_id")
    private Code code;

    @Column(name = "code_idx")
    private int codeIdx;

    @Column(name = "code_value")
    private String codeValue;


    public ResponseDomainCode() {
    }


    public ResponseDomainCode(int codeIdx, ResponseDomain responseDomain, Code code) {
        this.codeIdx = codeIdx;
        this.responseDomain = responseDomain;
        this.code = code;
    }

    public int getCodeIdx() {
        return codeIdx;
    }

    public void setCodeIdx(int codeIdx) {
        this.codeIdx = codeIdx;
    }

    public String getCodeValue() {
        return codeValue;
    }

    public void setCodeValue(String codeValue) {
        this.codeValue = codeValue;
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
        if (!(o instanceof ResponseDomainCode)) return false;
        if (!super.equals(o)) return false;

        ResponseDomainCode that = (ResponseDomainCode) o;

        if (getCodeIdx() != that.getCodeIdx()) return false;
        if (getResponseDomain() != null ? !getResponseDomain().equals(that.getResponseDomain()) : that.getResponseDomain() != null)
            return false;
        if (getCode() != null ? !getCode().equals(that.getCode()) : that.getCode() != null) return false;
        return !(getCodeValue() != null ? !getCodeValue().equals(that.getCodeValue()) : that.getCodeValue() != null);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getResponseDomain() != null ? getResponseDomain().hashCode() : 0);
        result = 31 * result + (getCode() != null ? getCode().hashCode() : 0);
        result = 31 * result + getCodeIdx();
        result = 31 * result + (getCodeValue() != null ? getCodeValue().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ResponseDomainCode{" +
                ", codeIdx=" + codeIdx +
                ", responseDomain=" + responseDomain +
                ", code=" + code +
                '}';
    }
}