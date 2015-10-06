package no.nsd.qddt.domain.agency.audit;

import no.nsd.qddt.domain.agency.Agency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
@Service("agencyAuditService")
class AgencyAuditServiceImpl implements AgencyAuditService {

    private AgencyAuditRepository agencyAuditRepository;

    @Autowired
    AgencyAuditServiceImpl(AgencyAuditRepository agencyAuditRepository){
        this.agencyAuditRepository = agencyAuditRepository;
    }

    @Override
    public Revision<Integer, Agency> findLastChange(UUID uuid) {
        return agencyAuditRepository.findLastChangeRevision(uuid);
    }

    @Override
    public Revision<Integer, Agency> findRevision(UUID uuid, Integer revision) {
        return agencyAuditRepository.findRevision(uuid, revision);
    }

    @Override
    public Page<Revision<Integer, Agency>> findRevisions(UUID uuid, Pageable pageable) {
        return agencyAuditRepository.findRevisions(uuid, pageable);
    }
}
