package no.nsd.qddt.domain.controlconstruct.json;

import no.nsd.qddt.domain.controlconstruct.pojo.ControlConstruct;
import no.nsd.qddt.domain.controlconstruct.pojo.Sequence;
import no.nsd.qddt.domain.controlconstruct.pojo.SequenceKind;
import no.nsd.qddt.domain.elementref.ElementRefImpl;

import java.util.List;

/**
 * @author Stig Norland
 */
public class ConstructSequenceJsonView extends ConstructJsonView {

    private List<ElementRefImpl<ControlConstruct>> sequence;

    private String description;

    private SequenceKind sequenceKind;

    public ConstructSequenceJsonView(Sequence construct) {
        super( construct );
        sequenceKind =  construct.getSequenceKind();
        sequence = construct.getSequence();
        description = construct.getDescription();
    }

    public String getDescription() {
        return description;
    }

    public SequenceKind getSequenceKind() {
        return sequenceKind;
    }

    public List<ElementRefImpl<ControlConstruct>> getSequence() {
        return sequence;
    }

}
