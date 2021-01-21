package no.nsd.qddt.domain.instrument;

import no.nsd.qddt.domain.instrument.pojo.Instrument;
import no.nsd.qddt.domain.instrument.pojo.InstrumentViewJson;
import no.nsd.qddt.domain.classes.interfaces.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
public interface InstrumentService extends BaseService<Instrument, UUID> {

    List<InstrumentViewJson> findByStudy(UUID studyId);

    Page<InstrumentViewJson> findAllPageable(Pageable pageable);

    Page<InstrumentViewJson> findByNameAndDescriptionPageable(String name, String description, String strKind, String xmlLang, Pageable pageable);

}
