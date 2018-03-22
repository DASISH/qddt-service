package no.nsd.qddt.domain.instrumentelement;

import no.nsd.qddt.domain.BaseService;
import no.nsd.qddt.domain.controlconstruct.pojo.Sequence;
import no.nsd.qddt.domain.elementref.ElementRef;
import no.nsd.qddt.domain.elementref.typed.ElementRefTyped;

import java.util.List;
import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
public interface InstrumentElementService extends BaseService<InstrumentElement, UUID> {

    ElementRef getDetail(ElementRef element);

    List<ElementRef> loadSequence(ElementRefTyped<Sequence> sequence);
}
