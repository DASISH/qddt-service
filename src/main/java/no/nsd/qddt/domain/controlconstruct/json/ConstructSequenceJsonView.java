package no.nsd.qddt.domain.controlconstruct.json;

import no.nsd.qddt.domain.controlconstruct.pojo.ControlConstruct;
import no.nsd.qddt.domain.controlconstruct.pojo.Sequence;
import no.nsd.qddt.domain.controlconstruct.pojo.SequenceKind;
import no.nsd.qddt.domain.classes.elementref.ElementRefEmbedded;

import java.util.List;

/**
 * @author Stig Norland
 */
public class ConstructSequenceJsonView extends ConstructJsonView {

    private final List<ElementRefEmbedded<ControlConstruct>> sequence;

    private final String description;

    private final SequenceKind sequenceKind;

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

    public List<ElementRefEmbedded<ControlConstruct>> getSequence() {
        return sequence;
    }

}
