package no.nsd.qddt.domain.controlconstruct.json;

import no.nsd.qddt.domain.controlconstruct.pojo.ControlConstruct;
import no.nsd.qddt.domain.controlconstruct.pojo.Sequence;
import no.nsd.qddt.domain.controlconstruct.pojo.SequenceKind;
import no.nsd.qddt.domain.elementref.ElementRefImpl;
import no.nsd.qddt.domain.universe.Universe;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Stig Norland
 */
public class ConstructSequenceJson extends ConstructJson {

    /**
     *
     */
    private static final long serialVersionUID = -7704864346435586898L;

    private List<ElementRefImpl<ControlConstruct>> sequence;

    private String description;

    private SequenceKind sequenceKind;

    private String universe;

    public ConstructSequenceJson(Sequence construct) {
        super( construct );
        sequenceKind =  construct.getSequenceKind();
        sequence = construct.getSequence();
        description = construct.getDescription();
        universe =  construct.getUniverse().stream().map( Universe::getDescription ).collect( Collectors.joining("/ ") );
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

    public String getUniverse() {
        return universe;
    }
}
