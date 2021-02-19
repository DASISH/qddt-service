package no.nsd.qddt.domain.universe;

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
@Service("universeService")
class UniverseServiceImpl implements UniverseService {

    private final UniverseRepository universeRepository;

    @Autowired
    public UniverseServiceImpl(UniverseRepository universeRepository) {
        this.universeRepository = universeRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return universeRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean exists(UUID id) {
        return universeRepository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Universe findOne(UUID id) {
        return universeRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(id, Universe.class));
    }

    @Override
    @Transactional()
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR')  and hasPermission(#instance,'AGENCY')")
    public Universe save(Universe instance) {
        return universeRepository.save(instance);
    }


    @Override
    @Transactional()
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR')")
    public void delete(UUID id) {
        universeRepository.deleteById(id);
    }


    protected Universe prePersistProcessing(Universe instance) {
        return instance;
    }


    protected Universe postLoadProcessing(Universe instance) {
        return instance;
    }

    @Override
    public Page<Universe> findByDescriptionLike(String description, String xmlLang, Pageable pageable) {
        return universeRepository.findByDescriptionIgnoreCaseLikeAndXmlLangLike(likeify(description), likeify(xmlLang), pageable);
    }
}
