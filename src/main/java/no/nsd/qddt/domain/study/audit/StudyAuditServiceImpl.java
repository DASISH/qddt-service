package no.nsd.qddt.domain.study.audit;

import no.nsd.qddt.domain.study.Study;
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
}
