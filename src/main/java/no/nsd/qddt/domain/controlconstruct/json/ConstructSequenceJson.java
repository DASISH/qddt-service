package no.nsd.qddt.domain.controlconstruct.json;

import no.nsd.qddt.domain.controlconstruct.pojo.ControlConstruct;
import no.nsd.qddt.domain.controlconstruct.pojo.Sequence;
import no.nsd.qddt.domain.controlconstruct.pojo.SequenceKind;
import no.nsd.qddt.domain.elementref.ElementRef;

import java.util.List;

/**
 * @author Stig Norland
 */
public class ConstructSequenceJson extends ConstructJson {

    /**
     *
     */
    private static final long serialVersionUID = -7704864346435586898L;

    private List<ElementRef<ControlConstruct>> sequence;

    private String description;

    private SequenceKind sequenceKind;

    public ConstructSequenceJson(Sequence construct) {
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

    public List<ElementRef<ControlConstruct>> getSequence() {
        return sequence;
    }

}
