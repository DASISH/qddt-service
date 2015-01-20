package no.nsd.qddt.domain.response;

import no.nsd.qddt.domain.AbstractEntity;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


/**
 *
 * CodeList A special form of maintainable that allows a single codelist to be maintained outside of a CodeListScheme.
 *
 * @author Stig Norland
 */

@Audited
@Entity
@Table(name = "ResponsDomain")
public class ResponseDomain extends AbstractEntity {


    @ManyToOne
    @JoinColumn(name = "respons_kind_id")
    private ResponseKind responseKind;


    @OneToMany(mappedBy="Code", cascade = CascadeType.ALL)
    private Set<Code> code = new HashSet<>();

/*    @ManyToMany(mappedBy="categoryCode", cascade = CascadeType.ALL)
    private Set<CategoryCode> categoryCode = new HashSet<>();
*/

    public ResponseKind getResponseKind() {
        return responseKind;
    }

    public void setResponseKind(ResponseKind responseKind) {
        this.responseKind = responseKind;
    }

    public Set<Code> getCode() {
        return code;
    }

    public void setCode(Set<Code> code) {
        this.code = code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ResponseDomain that = (ResponseDomain) o;

        if (code != null ? !code.equals(that.code) : that.code != null) return false;
        if (responseKind != null ? !responseKind.equals(that.responseKind) : that.responseKind != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (responseKind != null ? responseKind.hashCode() : 0);
        result = 31 * result + (code != null ? code.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ResponsDomain{" +
                "responsKind=" + responseKind +
                ", code=" + code +
                '}';
    }
}
