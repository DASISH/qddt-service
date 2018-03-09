package no.nsd.qddt.domain.controlconstruct.json;

import no.nsd.qddt.domain.AbstractJsonEdit;
import no.nsd.qddt.domain.controlconstruct.ControlConstruct;
import no.nsd.qddt.domain.controlconstruct.ControlConstructKind;
import no.nsd.qddt.domain.controlconstruct.SequenceKind;

/**
 * @author Stig Norland
 */
public class ConstructJson  extends AbstractJsonEdit {

    private ControlConstructKind controlConstructKind;

    private SequenceKind sequenceKind;

    public ConstructJson(ControlConstruct construct){
        super(construct);
        controlConstructKind = construct.getControlConstructKind();
        sequenceKind = construct.getSequenceEnum();
    }

    public ControlConstructKind getControlConstructKind() {
        return controlConstructKind;
    }

    public void setControlConstructKind(ControlConstructKind controlConstructKind) {
        this.controlConstructKind = controlConstructKind;
    }

    public String getSequenceKind() {
        return sequenceKind.getName();
    }

    public void setSequenceKind(String sequence) {
         this.sequenceKind= SequenceKind.getEnum(sequence);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConstructJson)) return false;

        ConstructJson that = (ConstructJson) o;

        return controlConstructKind == that.controlConstructKind && sequenceKind == that.sequenceKind;
    }

    @Override
    public int hashCode() {
        int result = controlConstructKind != null ? controlConstructKind.hashCode() : 0;
        result = 31 * result + (sequenceKind != null ? sequenceKind.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "{" + super.toString() +
                ", controlConstructKind=" + controlConstructKind +
                ", sequenceKind=" + sequenceKind +
                "} " ;
    }
}
