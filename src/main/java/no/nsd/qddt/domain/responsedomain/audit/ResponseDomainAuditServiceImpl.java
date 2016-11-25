package no.nsd.qddt.domain.responsedomain.audit;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.responsedomain.ResponseDomain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

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
        Revision<Integer, ResponseDomain> retval = responseDomainAuditRepository.findLastChangeRevision(uuid);
        retval.getEntity().getManagedRepresentation();
        return retval;
    }

    @Override
    public Revision<Integer, ResponseDomain> findRevision(UUID uuid, Integer revision) {
        Revision<Integer,ResponseDomain> retval = responseDomainAuditRepository.findRevision(uuid, revision);
        retval.getEntity().getManagedRepresentation();
        return retval;
    }

    @Override
    public Page<Revision<Integer, ResponseDomain>> findRevisions(UUID uuid, Pageable pageable) {
        System.out.println("findRevisions");
        Page<Revision<Integer, ResponseDomain>> retvals =responseDomainAuditRepository.findRevisions(uuid,pageable);

        retvals.forEach(c->{
            c.getEntity().getManagedRepresentation();
            System.out.println(c.getEntity());
        });

        return retvals;
    }

    @Override
    public Page<Revision<Integer, ResponseDomain>> findRevisionByIdAndChangeKindNotIn(UUID id, Collection<AbstractEntityAudit.ChangeKind> changeKinds, Pageable pageable) {
        int skip = pageable.getOffset();
        int limit = pageable.getPageSize();
        return new PageImpl<>(
                responseDomainAuditRepository.findRevisions(id).getContent().stream()
                        .filter(f -> !changeKinds.contains(f.getEntity().getChangeKind()))
                        .skip(skip)
                        .limit(limit)
                        .collect(Collectors.toList())
        );
    }

}
