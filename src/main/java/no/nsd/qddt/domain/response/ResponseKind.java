package no.nsd.qddt.domain.response;

import org.hibernate.annotations.*;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * A ResponseKind define what kind of ResponseDomain (answer) this is,
 * this will also define the way the Question is formatted.
 *
 * This Class would be a good candidate to change into a ENUM, as every entry
 * will have to be mapped to specific behaviour in GUI and intruments/surveys.
 *<dl>
 *      <dt>Category</dt><dd>A category (without an attached code) response for a question item.</dd>
 *      <dt>Code</dt><dd>A coded response (where both codes and their related category value are displayed) for a question item.</dd>
 *      <dt>Numeric</dt><dd>A numeric response (the intent is to analyze the response as a number) for a question item.</dd>
 *      <dt>Scale</dt><dd>A scale response which describes a 1..n dimensional scale of various display types for a question.</dd>
 *      <dt>Text</dt><dd>A textual response.</dd>
 * </dl>

 * @author Stig Norland
 * @author Dag Østgulen Heradstveit
 */
@Audited//(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@Entity
@Table(name = "response_kind")
public class ResponseKind {

    @Id
    @Column(name = "id")
    @Type(type="pg-uuid")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2", parameters = {
            @org.hibernate.annotations.Parameter(name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy") })
    private UUID id;

    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "responseKind", cascade = CascadeType.ALL)
    private Set<ResponseDomain> response = new HashSet<>();

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
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
