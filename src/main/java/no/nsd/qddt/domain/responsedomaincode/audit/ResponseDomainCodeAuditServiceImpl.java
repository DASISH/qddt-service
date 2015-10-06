package no.nsd.qddt.domain.responsedomaincode.audit;

import no.nsd.qddt.domain.responsedomaincode.ResponseDomainCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
@Service("responseDomainCodeAuditService")
class ResponseDomainCodeAuditServiceImpl implements ResponseDomainCodeAuditService {

    private ResponseDomainCodeAuditRepository responseDomainCodeAuditRepository;

    @Autowired
    public ResponseDomainCodeAuditServiceImpl(ResponseDomainCodeAuditRepository responseDomainCodeAuditRepository) {
        this.responseDomainCodeAuditRepository = responseDomainCodeAuditRepository;
    }

    @Override
    public Revision<Integer, ResponseDomainCode> findLastChange(UUID uuid) {
        return responseDomainCodeAuditRepository.findLastChangeRevision(uuid);
    }

    @Override
    public Revision<Integer, ResponseDomainCode> findRevision(UUID uuid, Integer revision) {
        return responseDomainCodeAuditRepository.findRevision(uuid, revision);
    }

    @Override
    public Page<Revision<Integer, ResponseDomainCode>> findRevisions(UUID uuid, Pageable pageable) {
        return responseDomainCodeAuditRepository.findRevisions(uuid, pageable);
    }
}