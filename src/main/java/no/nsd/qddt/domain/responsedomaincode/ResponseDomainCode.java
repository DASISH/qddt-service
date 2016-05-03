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

//    @ManyToOne
//    @JoinColumn(name = "responsedomain_id")
//    private ResponseDomain responseDomain;

    @ManyToOne
    @JoinColumn(name="question_id")
    private Question question;

    @ManyToOne
    @OrderColumn(name="category_index")
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "category_index")
    private int categoryIndex;

    @Column(name = "code_value")
    private String codeValue;

    public ResponseDomainCode() {

    }

    public ResponseDomainCode( Question question, Category category){

        this.category = category;
    }

    public ResponseDomainCode(int categoryIndex, Question question, Category category) {
        this.categoryIndex = categoryIndex;

        this.category = category;
    }

    public int getCategoryIndex() {
        return categoryIndex;
    }

    public void setCategoryIndex(int categoryIndex) {
        this.categoryIndex = categoryIndex;
    }

    public String getCodeValue() {
        return codeValue;
    }

    public void setCodeValue(String codeValue) {
        this.codeValue = codeValue;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ResponseDomainCode)) return false;
        if (!super.equals(o)) return false;

        ResponseDomainCode that = (ResponseDomainCode) o;

        if (categoryIndex != that.categoryIndex) return false;
        if (question != null ? !question.equals(that.question) : that.question != null)
            return false;
        if (category != null ? !category.equals(that.category) : that.category != null) return false;
        return !(codeValue != null ? !codeValue.equals(that.codeValue) : that.codeValue != null);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (question != null ? question.hashCode() : 0);
        result = 31 * result + (category != null ? category.hashCode() : 0);
        result = 31 * result + categoryIndex;
        result = 31 * result + (codeValue != null ? codeValue.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ResponseDomainCode{" +
                ", category=" + category +
                ", categoryIndex=" + categoryIndex +
                ", codeValue='" + codeValue + '\'' +
                question +
                "} " + super.toString();
    }
}