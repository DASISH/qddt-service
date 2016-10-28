package no.nsd.qddt.domain.selectable;

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
public class SelectableServiceImpl implements SelectableService {

    private SelectableRepository repository;

    @Autowired
    public SelectableServiceImpl(SelectableRepository repository) {
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
    public Selectable findOne(UUID uuid) {
        return repository.findOne(uuid);
    }

    @Override
    public Selectable save(Selectable instance) {
        return repository.save(instance);
    }

    @Override
    public List<Selectable> save(List<Selectable> instances) {
        return repository.save(instances);
    }

    @Override
    public void delete(UUID uuid) {
        repository.delete(uuid);
    }

    @Override
    public void delete(List<Selectable> instances) {
        repository.delete(instances);
    }

    @Override
    public Page<Selectable> findAllPageable(Pageable pageable) {
        return repository.findAll(pageable);
    }
}
