package no.nsd.qddt.domain.response;

import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * A ResponseKind define what kind of ResponseDomain (answer) this is,
 * this will also define the way the Question is formatted.
 *
 * This Class would be a good candidate to change into a ENUM, as every entry
 * will have to be mapped to specific behaviour in GUI and intruments/surveys.
 *
 * @author Stig Norland
 * @author Dag Østgulen Heradstveit
 */
@Audited//(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@Entity
@Table(name = "ResponseKind")
public class ResponseKind {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "responseKind", cascade = CascadeType.ALL)
    private Set<ResponseDomain> response = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<ResponseDomain> getResponse() {
        return response;
    }

    public void setResponse(Set<ResponseDomain> response) {
        this.response = response;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ResponseKind that = (ResponseKind) o;

        if (code != null ? !code.equals(that.code) : that.code != null) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ResponsKind{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
