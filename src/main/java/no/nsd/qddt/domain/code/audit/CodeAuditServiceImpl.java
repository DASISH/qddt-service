package no.nsd.qddt.domain.code.audit;

import no.nsd.qddt.domain.code.Code;
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
@Service("codeAuditService")
class CodeAuditServiceImpl implements CodeAuditService {

    private CodeAuditRepository codeAuditRepository;

    @Autowired
    public CodeAuditServiceImpl(CodeAuditRepository codeRepository) {
        this.codeAuditRepository = codeRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Revision<Integer, Code> findLastChange(UUID uuid) {
        return codeAuditRepository.findLastChangeRevision(uuid);
    }

    @Override
    @Transactional(readOnly = true)
    public Revision<Integer, Code> findRevision(UUID uuid, Integer revision) {
        return codeAuditRepository.findRevision(uuid, revision);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Revision<Integer, Code>> findRevisions(UUID uuid, Pageable pageable) {
        return codeAuditRepository.findRevisions(uuid,pageable);
    }
}
