package no.nsd.qddt.domain.publication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * @author Stig Norland
 */
@Service("selectableService")
public class PublicationServiceImpl implements PublicationService {

    private PublicationRepository repository;

    @Autowired
    public PublicationServiceImpl(PublicationRepository repository) {
        this.repository = repository;
    }

    @Override
    public long count() {
        return repository.count();
    }

    @Override
    public boolean exists(UUID uuid) {
        return repository.exists(uuid);
    }

    @Override
    public Publication findOne(UUID uuid) {
        return repository.findOne(uuid);
    }

    @Override
    public Publication save(Publication instance) {
        return repository.save(instance);
    }

    @Override
    public List<Publication> save(List<Publication> instances) {
        return repository.save(instances);
    }

    @Override
    public void delete(UUID uuid) {
        repository.delete(uuid);
    }

    @Override
    public void delete(List<Publication> instances) {
        repository.delete(instances);
    }

    @Override
    public Page<Publication> findAllPageable(Pageable pageable) {
        return repository.findAll(pageable);
    }
}
