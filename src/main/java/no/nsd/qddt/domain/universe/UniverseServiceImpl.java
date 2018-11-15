package no.nsd.qddt.domain.universe;

import no.nsd.qddt.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
    public boolean exists(UUID uuid) {
        return universeRepository.exists(uuid);
    }

    @Override
    @Transactional(readOnly = true)
    public Universe findOne(UUID uuid) {
        return universeRepository.findById(uuid).orElseThrow(
                () -> new ResourceNotFoundException(uuid, Universe.class));
    }

    @Override
    @Transactional()
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR')  and hasPermission(#instance,'AGENCY')")
    public Universe save(Universe universe) {
        return universeRepository.save(universe);
    }


//    @Transactional()
//    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR')")
//    public List<Universe> save(List<Universe> universes) {
//        return universeRepository.save(universes);
//    }

    @Override
    @Transactional()
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR')")
    public void delete(UUID uuid) {
        universeRepository.delete(uuid);
    }

    @Override
    @Transactional()
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR')")
    public void delete(List<Universe> universes) {
        universeRepository.delete(universes);
    }


    protected Universe prePersistProcessing(Universe instance) {
        return instance;
    }


    protected Universe postLoadProcessing(Universe instance) {
        return instance;
    }

    @Override
    public Page<Universe> findByDescriptionLike(String description, Pageable pageable) {
        return universeRepository.findByDescriptionIgnoreCaseLike(likeify(description),pageable);
    }
}
