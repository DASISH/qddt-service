package no.nsd.qddt.domain.response;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;

/**
 * @author Dag Østgulen Heradstveit
 */
@Embeddable
public class ResponseDomainCodeId implements Serializable {

    @ManyToOne
    private ResponseDomain responseDomain;

    @ManyToOne
    private Code code;

    public ResponseDomainCodeId() {
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

        ResponseDomainCodeId that = (ResponseDomainCodeId) o;

        if (code != null ? !code.equals(that.code) : that.code != null) return false;
        if (responseDomain != null ? !responseDomain.equals(that.responseDomain) : that.responseDomain != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = responseDomain != null ? responseDomain.hashCode() : 0;
        result = 31 * result + (code != null ? code.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ResponseDomainCodeId{" +
                "responseDomain=" + responseDomain +
                ", code=" + code +
                '}';
    }
}
