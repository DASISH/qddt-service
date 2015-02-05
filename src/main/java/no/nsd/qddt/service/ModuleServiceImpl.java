package no.nsd.qddt.service;

import no.nsd.qddt.domain.Module;
import no.nsd.qddt.exception.ResourceNotFoundException;
import no.nsd.qddt.repository.ModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * @author Stig Norland
 */
@Service("moduleService")
public class ModuleServiceImpl implements ModuleService {

    private ModuleRepository moduleRepository;

    @Autowired
    public ModuleServiceImpl(ModuleRepository moduleRepository) {
        this.moduleRepository = moduleRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Module findById(Long id) {
        return moduleRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(id, Module.class)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<Module> findAll() {
        return moduleRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Module> findAll(Pageable pageable) { return moduleRepository.findAll(pageable); }

    @Override
    @Transactional(readOnly = false)
    public Module save(Module instance) {

        instance.setCreated(LocalDateTime.now());
        return moduleRepository.save(instance);
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(Module instance) { moduleRepository.delete(instance);  }

    @Override
    @Transactional(readOnly = true)
    public Module findByGuid(UUID id) {
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public Revision<Integer, Module> findLastChange(Long id) {
        return moduleRepository.findLastChangeRevision(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Revision<Integer, Module> findEntityAtRevision(Long id, Integer revision) {
        return moduleRepository.findEntityAtRevision(id, revision);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Revision<Integer, Module>> findAllRevisionsPageable(Long id, Pageable pageable) {
        return null;
    }

}
