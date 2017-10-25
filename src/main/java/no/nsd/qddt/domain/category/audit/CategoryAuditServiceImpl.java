package no.nsd.qddt.domain.category.audit;

import no.nsd.qddt.domain.category.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static no.nsd.qddt.utils.FilterTool.defaultOrModifiedSort;

/**
 * @author Dag Østgulen Heradstveit
 */
@Service("categoryAuditService")
class CategoryAuditServiceImpl implements CategoryAuditService {

    private final CategoryAuditRepository categoryAuditRepository;

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
        return categoryAuditRepository.findRevisions(uuid,defaultOrModifiedSort(pageable,"updated ASC"));
    }

    @Override
    public Revision<Integer, Category> findFirstChange(UUID uuid) {
        return categoryAuditRepository.findRevisions(uuid)
            .reverse().getContent().get(0);
    }

    // Categories most likely don't have discussions about them... and you are not often interested in old versions of a category,
    // hence we don't need to fetch comments that never are there...
    @Override
    public void setShowPrivateComment(boolean showPrivate) {
        // no implementation
    }

}
