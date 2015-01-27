package no.nsd.qddt.service;

import no.nsd.qddt.domain.Module;
import no.nsd.qddt.repository.ModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.history.Revision;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        return moduleRepository.findOne(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Module> findAll() {
        return moduleRepository.findAll();
    }

    @Override
    @Transactional(readOnly = false)
    public Module save(Module module) {
        return moduleRepository.save(module);
    }

    @Override
    @Transactional(readOnly = true)
    public Revision<Integer, Module> findLastChange(Long id) {
        return moduleRepository.findLastChangeRevision(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Revision<Integer, Module>> findAllRevisionsPageable(Module module, int page, int size) {
        return moduleRepository.findRevisions(module.getId(),new PageRequest(page, size));
    }
}
