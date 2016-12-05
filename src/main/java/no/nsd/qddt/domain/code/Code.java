package no.nsd.qddt.domain.code;

import com.fasterxml.jackson.annotation.JsonBackReference;
import no.nsd.qddt.domain.AbstractEntity;
import no.nsd.qddt.domain.responsedomain.ResponseDomain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.envers.Audited;

import javax.persistence.*;


/**
 *
 * @author Dag Østgulen Heradstveit
 * @author Stig Norland
 */
@Audited
@Entity
@Table(name = "CODE")
public class Code  extends AbstractEntity implements CodeJson {

    @JsonBackReference
    @ManyToOne(optional = false, cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "responsedomain_id")
    private ResponseDomain responseDomain;

    // Ordered arrayList doesn't work with Enver FIX
    @Column(insertable = false,updatable = false)
    private int responsedomain_idx;

    @Column(name = "code_value")
    private String codeValue;


    public Code() {
        codeValue = "";
    }


    public Code(ResponseDomain responseDomain, String codeValue){
        this.setCodeValue(codeValue);
        this.setResponseDomain(responseDomain);
    }

    @JsonIgnore
    public int getResponsedomain_idx() {
        return responsedomain_idx;
    }

    public void setResponsedomain_idx(int responsedomain_idx) {
        this.responsedomain_idx = responsedomain_idx;
    }

    public String getCodeValue() {
        return codeValue;
    }

    public void setCodeValue(String codeValue) {
        this.codeValue = codeValue;
    }


    @JsonIgnore
    public ResponseDomain getResponseDomain() {
        return responseDomain;
    }

    public void setResponseDomain(ResponseDomain responseDomain) {
        this.responseDomain = responseDomain;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Code)) return false;
        if (!super.equals(o)) return false;

        Code code = (Code) o;

        return codeValue != null ? codeValue.equals(code.codeValue) : code.codeValue == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (codeValue != null ? codeValue.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return String.format("Code { responsedomain_idx = %d, codeValue = %s }" ,responsedomain_idx,getCodeValue());
    }
}