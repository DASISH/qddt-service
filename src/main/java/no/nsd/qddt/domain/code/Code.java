package no.nsd.qddt.domain.code;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.responsedomaincode.ResponseDomainCode;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * CategoryScheme : Categories provide enumerated representations for
 *      concepts and are used by questions, code lists, and variables
 *
 * CodeListScheme : Code lists link a specific value with a category and
 *      are used by questions and variables
 *
 * ManagedRepresentationScheme : Reusable representations of numeric,
 *      textual datetime, scale or missing values types.
 *
 * CodeType (aka Code) A structure that links a unique value of a code to a
 * specified category and provides information as to the location of the code
 * within a hierarchy, whether it is discrete, represents a total for the CodeList contents,
 * and if its sub-elements represent a comprehensive coverage of the code.
 * The Code is identifiable, but the value within the code must also be unique within the CodeList.
 *
 *
 * @author Stig Norland
 * @author Dag Østgulen Heradstveit
 */
@Audited
@Entity
@Table(name = "CODE")
public class Code extends AbstractEntityAudit {

    @OneToMany(mappedBy="code", cascade = CascadeType.ALL)
    private Set<ResponseDomainCode> responseDomainCodes = new HashSet<>();

    @Column(name = "category")
    private String category;

    @Column(name = "description", length = 2000)
    private String description;

    public Code() {

    }

    /**
     *
     * @param tag tags
     * @param category a category
     */
    public Code(String tag, String category){
        setCategory(category);
        setName(tag);
    }


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<ResponseDomainCode> getResponseDomainCodes() {
        return responseDomainCodes;
    }

    public void setResponseDomainCodes(Set<ResponseDomainCode> responseDomainCodes) {
        this.responseDomainCodes = responseDomainCodes;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Code)) return false;
        if (!super.equals(o)) return false;

        Code code = (Code) o;
        if (description != null ? !description.equals(code.description) : code.description != null) return false;
        return !(category != null ? !category.equals(code.category) : code.category != null);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (category != null ? category.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Code{" +
                " category='" + category + '\'' +
                ", description='" + description + '\'' +
                "} " + super.toString();
    }


}
