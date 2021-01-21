package no.nsd.qddt.domain.instruction;

import no.nsd.qddt.domain.classes.interfaces.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 * @author Stig Norland
 */
public interface InstructionService extends BaseService<Instruction, UUID> {

    Page<Instruction> findByDescriptionLike(String description, String xmlLang, Pageable pageable);
}
