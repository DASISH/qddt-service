package no.nsd.qddt.domain.instrument;

import no.nsd.qddt.domain.BaseService;

import java.util.List;
import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
public interface InstrumentService extends BaseService<Instrument, UUID> {

    List<Instrument> findByStudy(UUID studyId);
}
