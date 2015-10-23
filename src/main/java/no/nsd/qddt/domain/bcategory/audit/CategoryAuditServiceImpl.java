package no.nsd.qddt.domain.bcategory.audit;

import no.nsd.qddt.domain.bcategory.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
@Service("categoryAuditService")
class CategoryAuditServiceImpl implements CategoryAuditService {

    private CategoryAuditRepository categoryAuditRepository;

    @Autowired
    public CategoryAuditServiceImpl(CategoryAuditRepository repository) {
        this.categoryAuditRepository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public Revision<Integer, Category> findLastChange(UUID uuid) {
        return categoryAuditRepository.findLastChangeRevision(uuid);
    }

    @Override
    @Transactional(readOnly = true)
    public Revision<Integer, Category> findRevision(UUID uuid, Integer revision) {
        return categoryAuditRepository.findRevision(uuid, revision);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Revision<Integer, Category>> findRevisions(UUID uuid, Pageable pageable) {
        return categoryAuditRepository.findRevisions(uuid,pageable);
    }
}
