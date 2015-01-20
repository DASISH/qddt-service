package no.nsd.qddt.domain.respons;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import no.nsd.qddt.domain.AbstractEntity;
import no.nsd.qddt.domain.ChangeReason;
import no.nsd.qddt.domain.User;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


/**
 * @author Stig Norland
 */
@Audited
@Entity
@Table(name = "ResponsDomain")
public class ResponsDomain  extends AbstractEntity {


    @ManyToOne
    @JoinColumn(name = "respons_kind_id")
    private ResponsKind responsKind;


    @OneToMany(mappedBy="Code", cascade = CascadeType.ALL)
    private Set<Code> code = new HashSet<>();

/*    @ManyToMany(mappedBy="categoryCode", cascade = CascadeType.ALL)
    private Set<CategoryCode> categoryCode = new HashSet<>();
*/

    public ResponsKind getResponsKind() {
        return responsKind;
    }

    public void setResponsKind(ResponsKind responsKind) {
        this.responsKind = responsKind;
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

        ResponsDomain that = (ResponsDomain) o;

        if (code != null ? !code.equals(that.code) : that.code != null) return false;
        if (responsKind != null ? !responsKind.equals(that.responsKind) : that.responsKind != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (responsKind != null ? responsKind.hashCode() : 0);
        result = 31 * result + (code != null ? code.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ResponsDomain{" +
                "responsKind=" + responsKind +
                ", code=" + code +
                '}';
    }
}
