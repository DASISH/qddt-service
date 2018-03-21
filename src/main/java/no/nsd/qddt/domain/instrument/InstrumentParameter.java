package no.nsd.qddt.domain.instrument;

import org.hibernate.envers.Audited;

import javax.persistence.Embeddable;
import java.util.UUID;

/**
 * @author Stig Norland
 */
@Audited
@Embeddable
public class InstrumentParameter {

    private String name;

    private UUID parameterId;

    private Integer parameterIdx;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getParameterId() {
        return parameterId;
    }

    public void setParameterId(UUID parameterId) {
        this.parameterId = parameterId;
    }

    public Integer getParameterIdx() {
        return parameterIdx;
    }

    public void setParameterIdx(Integer parameterIdx) {
        this.parameterIdx = parameterIdx;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InstrumentParameter)) return false;

        InstrumentParameter that = (InstrumentParameter) o;

        if (name != null ? !name.equals( that.name ) : that.name != null) return false;
        if (parameterId != null ? !parameterId.equals( that.parameterId ) : that.parameterId != null) return false;
        return parameterIdx != null ? parameterIdx.equals( that.parameterIdx ) : that.parameterIdx == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (parameterId != null ? parameterId.hashCode() : 0);
        result = 31 * result + (parameterIdx != null ? parameterIdx.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "{\"ElementParameter\":{"
            + "\"name\":\"" + name + "\""
            + ", \"parameterId\":" + parameterId
            + ", \"parameterIdx\":\"" + parameterIdx + "\""
            + "}}";
    }


}
