package no.nsd.qddt.domain.responsedomaincode;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.category.Category;
import no.nsd.qddt.domain.question.Question;
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

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;


    @Column(name = "code_value")
    private String codeValue;

    public ResponseDomainCode() {

    }

    public ResponseDomainCode( ResponseDomain responseDomain, Category category, String codeValue){
        this.setCodeValue(codeValue);
        this.setCategory(category);
        this.setResponseDomain(responseDomain);
    }


    public String getCodeValue() {
        return codeValue;
    }

    public void setCodeValue(String codeValue) {
        this.codeValue = codeValue;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public ResponseDomain getResponseDomain() {
        return responseDomain;
    }

    public void setResponseDomain(ResponseDomain responseDomain) {
        this.responseDomain = responseDomain;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ResponseDomainCode)) return false;
        if (!super.equals(o)) return false;

        ResponseDomainCode that = (ResponseDomainCode) o;

        if (responseDomain != null ? !responseDomain.equals(that.responseDomain) : that.responseDomain != null)
            return false;
        if (category != null ? !category.equals(that.category) : that.category != null) return false;
        return codeValue != null ? codeValue.equals(that.codeValue) : that.codeValue == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (responseDomain != null ? responseDomain.hashCode() : 0);
        result = 31 * result + (category != null ? category.hashCode() : 0);
        result = 31 * result + (codeValue != null ? codeValue.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ResponseCode{" +
                ", category=" + category.getName() +
                ", code='" + codeValue + '\'' +
                "} ";
    }
}