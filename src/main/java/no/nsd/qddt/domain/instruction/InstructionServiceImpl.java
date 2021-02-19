package no.nsd.qddt.domain.instruction;

import no.nsd.qddt.domain.classes.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static no.nsd.qddt.utils.StringTool.likeify;

/**
 * @author Dag Østgulen Heradstveit
 */
@Service("instructionService")
class InstructionServiceImpl implements InstructionService {

    private final InstructionRepository instructionRepository;

    @Autowired
    public InstructionServiceImpl(InstructionRepository instructionRepository) {
        this.instructionRepository = instructionRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return instructionRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean exists(UUID id) {
        return instructionRepository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR','ROLE_CONCEPT','ROLE_VIEW')")
    public Instruction findOne(UUID id) {
        return instructionRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(id, Instruction.class));
    }

    @Override
    @Transactional()
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR')  and hasPermission(#instance,'AGENCY')")
    public Instruction save(Instruction instance) {
        return instructionRepository.save(instance);
    }


    @Override
    @Transactional()
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR')")
    public void delete(UUID id) {
        instructionRepository.deleteById(id);
    }


    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR','ROLE_CONCEPT','ROLE_VIEW')")
    public Page<Instruction> findByDescriptionLike(String description, String xmlLang, Pageable pageable) {
        return instructionRepository.findByDescriptionIgnoreCaseLikeAndXmlLangLike(likeify(description), likeify(xmlLang), pageable);
    }


    protected Instruction prePersistProcessing(Instruction instance) {
        return instance;
    }


    protected Instruction postLoadProcessing(Instruction instance) {
        return instance;
    }
}
