package no.nsd.qddt.domain.instruction;

import no.nsd.qddt.domain.BaseService;
import no.nsd.qddt.domain.instrument.Instrument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
public interface InstructionService extends BaseService<Instruction, UUID> {

    Page<Instruction> findByDescriptionLike(String description, Pageable pageable);
}
