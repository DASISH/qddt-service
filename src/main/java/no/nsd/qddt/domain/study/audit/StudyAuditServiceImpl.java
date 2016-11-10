package no.nsd.qddt.domain.study.audit;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.study.Study;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Dag Østgulen Heradstveit
 */
@Service("studyAuditService")
class StudyAuditServiceImpl implements StudyAuditService {

    private StudyAuditRepository studyAuditRepository;

    @Autowired
    public StudyAuditServiceImpl(StudyAuditRepository studyAuditRepository) {
        this.studyAuditRepository = studyAuditRepository;
    }


    @Override
    @Transactional(readOnly = true)
    public Revision<Integer, Study> findLastChange(UUID uuid) {
        return studyAuditRepository.findLastChangeRevision(uuid);
    }

    @Override
    @Transactional(readOnly = true)
    public Revision<Integer, Study> findRevision(UUID uuid, Integer revision) {
        return studyAuditRepository.findRevision(uuid, revision);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Revision<Integer, Study>> findRevisions(UUID uuid, Pageable pageable) {
        return studyAuditRepository.findRevisions(uuid,pageable);
    }

    @Override
    public Page<Revision<Integer, Study>> findRevisionByIdAndChangeKindNotIn(UUID id, Collection<AbstractEntityAudit.ChangeKind> changeKinds, Pageable pageable) {
        int skip = pageable.getOffset();
        int limit = pageable.getPageSize();
        return new PageImpl<>(
                studyAuditRepository.findRevisions(id).getContent().stream()
                        .filter(f -> !changeKinds.contains(f.getEntity().getChangeKind()))
                        .skip(skip)
                        .limit(limit)
                        .collect(Collectors.toList())
        );
    }


}
