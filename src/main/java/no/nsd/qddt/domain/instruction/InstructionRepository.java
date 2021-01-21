package no.nsd.qddt.domain.instruction;

import no.nsd.qddt.domain.classes.interfaces.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 * @author Stig Norland
 */
@Repository
interface InstructionRepository extends BaseRepository<Instruction, UUID> {

    Page<Instruction> findByDescriptionIgnoreCaseLikeAndXmlLangLike(String description, String xmlLang, Pageable pageable);
}

