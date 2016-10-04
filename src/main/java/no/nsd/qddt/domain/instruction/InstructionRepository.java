package no.nsd.qddt.domain.instruction;

import no.nsd.qddt.domain.BaseRepository;
import no.nsd.qddt.domain.instrument.Instrument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
@Repository
interface InstructionRepository extends BaseRepository<Instruction, UUID> {

    Page<Instruction> findByDescriptionLike(String description, Pageable pageable);
}

