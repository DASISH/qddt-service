package no.nsd.qddt.domain.controlconstruct.json;

import no.nsd.qddt.domain.elementref.IEntityRef;
import no.nsd.qddt.domain.controlconstruct.pojo.Sequence;
import no.nsd.qddt.domain.controlconstruct.pojo.SequenceKind;
import no.nsd.qddt.domain.elementref.ElementRef;

import java.util.List;

/**
 * @author Stig Norland
 */
public class ConstructSequenceJsonView extends ConstructJsonView {

    private List<ElementRef<IEntityRef>> sequence;

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

    public List<ElementRef<IEntityRef>> getSequence() {
        return sequence;
    }

}
