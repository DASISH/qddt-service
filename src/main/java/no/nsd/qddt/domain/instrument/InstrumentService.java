package no.nsd.qddt.domain.instrument;

import no.nsd.qddt.domain.BaseService;
import no.nsd.qddt.domain.controlconstruct.pojo.Sequence;
import no.nsd.qddt.domain.elementref.ElementRef;
import no.nsd.qddt.domain.elementref.typed.ElementRefTyped;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
public interface InstrumentService extends BaseService<Instrument, UUID> {

    List<Instrument> findByStudy(UUID studyId);

    Page<Instrument> findAllPageable(Pageable pageable);

    Page<Instrument> findByNameAndDescriptionPageable(String name, String description, Pageable pageable);

    ElementRef getDetail(ElementRef element);

    List<ElementRef> loadSequence(ElementRefTyped<Sequence> sequence );
}
