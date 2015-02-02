package no.nsd.qddt.repository;

import no.nsd.qddt.domain.Agency;
import no.nsd.qddt.domain.instrument.Instrument;
import org.springframework.data.envers.repository.support.EnversRevisionRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Stig Norland
 */
@Repository
public interface InstrumentRepository extends AbstractRepository<Instrument>, EnversRevisionRepository<Instrument, Long, Integer> {}

