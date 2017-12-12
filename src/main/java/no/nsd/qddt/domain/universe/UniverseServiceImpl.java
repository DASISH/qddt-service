package no.nsd.qddt.domain.universe;

import no.nsd.qddt.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

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
    public Universe save(Universe universe) {
        return universeRepository.save(universe);
    }

    @Override
    @Transactional()
    public List<Universe> save(List<Universe> universes) {
        return universeRepository.save(universes);
    }

    @Override
    @Transactional()
    public void delete(UUID uuid) {
        universeRepository.delete(uuid);
    }

    @Override
    @Transactional()
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
        return universeRepository.findByDescriptionIgnoreCaseLike(description,pageable);
    }
}
