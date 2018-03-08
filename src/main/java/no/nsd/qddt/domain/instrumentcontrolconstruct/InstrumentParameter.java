package no.nsd.qddt.domain.instrumentcontrolconstruct;


import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.UUID;

@Audited
@Embeddable
public class InstrumentParameter implements java.io.Serializable {

    private static final long serialVersionUID = -7261847349839337877L;

    private String name;

    // parameter read value from here....
    @Type(type="pg-uuid")
    @Column(name="param_ref")
    private UUID paramRef;


    public InstrumentParameter() {
    }

    public InstrumentParameter(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getParamRef() {
        return paramRef;
    }

    public void setParamRef(UUID paramRef) {
        this.paramRef = paramRef;
    }

    @Override
    public String toString() {
        return String.format(
            "InstrumentParameter (name=%s, ParameterReference=%s)", this.name, this.paramRef );
    }

}
