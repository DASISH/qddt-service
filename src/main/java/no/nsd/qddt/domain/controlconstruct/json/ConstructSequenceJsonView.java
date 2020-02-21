package no.nsd.qddt.domain.controlconstruct.json;

import no.nsd.qddt.domain.IElementRefType;
import no.nsd.qddt.domain.IEntityAuditXmlRef;
import no.nsd.qddt.domain.controlconstruct.pojo.Sequence;
import no.nsd.qddt.domain.controlconstruct.pojo.SequenceKind;
import no.nsd.qddt.domain.elementref.ElementRef;

import java.util.List;

/**
 * @author Stig Norland
 */
public class ConstructSequenceJsonView extends ConstructJsonView {

    private List<ElementRef<IElementRefType>> sequence;

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

    public List<ElementRef<IElementRefType>> getSequence() {
        return sequence;
    }

}
