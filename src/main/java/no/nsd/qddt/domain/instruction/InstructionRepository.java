package no.nsd.qddt.domain.instruction;

import no.nsd.qddt.domain.BaseRepository;
import no.nsd.qddt.domain.instrument.Instrument;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
@Repository
interface InstructionRepository extends BaseRepository<Instrument, UUID> {

}

