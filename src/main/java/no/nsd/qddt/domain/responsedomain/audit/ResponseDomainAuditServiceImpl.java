package no.nsd.qddt.domain.responsedomain.audit;

import no.nsd.qddt.domain.responsedomain.ResponseDomain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
@Service("responseDomainAuditService")
class ResponseDomainAuditServiceImpl implements ResponseDomainAuditService {

    private ResponseDomainAuditRepository responseDomainAuditRepository;

    @Autowired
    public ResponseDomainAuditServiceImpl(ResponseDomainAuditRepository responseDomainAuditRepository) {
        this.responseDomainAuditRepository = responseDomainAuditRepository;
    }

    @Override
    public Revision<Integer, ResponseDomain> findLastChange(UUID uuid) {
        return responseDomainAuditRepository.findLastChangeRevision(uuid);
    }

    @Override
    public Revision<Integer, ResponseDomain> findRevision(UUID uuid, Integer revision) {
        return responseDomainAuditRepository.findRevision(uuid, revision);
    }

    @Override
    public Page<Revision<Integer, ResponseDomain>> findRevisions(UUID uuid, Pageable pageable) {
        return responseDomainAuditRepository.findRevisions(uuid,pageable);
    }
}
