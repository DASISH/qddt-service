package no.nsd.qddt.domain.response;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RelationTargetAuditMode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * CategoryScheme : Categories provide enumerated representations for
 *      concepts andare used by questions, code lists, and variables
 *
 * CodeListScheme : Code lists link a specific value with a category and
 *      are used by questions and variables
 *
 * ManagedRepresentationScheme : Reusable representations of numeric,
 *      textual datetime, scale or missing values types.
 *
 * @author Stig Norland
 * @author Dag Østgulen Heradstveit
 */
@Audited
@Entity
@Table(name = "code")
public class Code implements Serializable {

    @Id
    @Column(name = "code_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String category;

    private  String code;

    @NotAudited
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "pk.code", cascade = CascadeType.ALL)
    private Set<ResponseDomainCode> responseDomainCodes = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Code code1 = (Code) o;

        if (category != null ? !category.equals(code1.category) : code1.category != null) return false;
        if (code != null ? !code.equals(code1.code) : code1.code != null) return false;
        if (id != null ? !id.equals(code1.id) : code1.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (category != null ? category.hashCode() : 0);
        result = 31 * result + (code != null ? code.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Code{" +
                "id=" + id +
                ", category='" + category + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
