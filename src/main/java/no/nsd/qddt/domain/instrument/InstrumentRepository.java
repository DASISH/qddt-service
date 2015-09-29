package no.nsd.qddt.domain.instrument;

import no.nsd.qddt.domain.BaseRepository;
import org.springframework.data.envers.repository.support.EnversRevisionRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author Stig Norland
 */
@Repository
interface InstrumentRepository extends BaseRepository<Instrument,UUID>, EnversRevisionRepository<Instrument, UUID, Integer> {}

