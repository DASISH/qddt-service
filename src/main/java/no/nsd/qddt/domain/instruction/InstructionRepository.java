package no.nsd.qddt.domain.instruction;

import no.nsd.qddt.domain.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
@Repository
interface InstructionRepository extends BaseRepository<Instruction, UUID> {

    Page<Instruction> findByDescriptionIgnoreCaseLike(String description, Pageable pageable);
}

