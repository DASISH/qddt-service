package no.nsd.qddt.domain.instrument.audit;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.instrument.Instrument;
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
@Service("instrumentAuditService")
class InstrumentAuditServiceImpl implements InstrumentAuditService {

    private InstrumentAuditRepository instrumentAuditRepository;

    @Autowired
    public InstrumentAuditServiceImpl(InstrumentAuditRepository instrumentAuditRepository) {
        this.instrumentAuditRepository = instrumentAuditRepository;
    }

    @Override
    public Revision<Integer, Instrument> findLastChange(UUID uuid) {
        return instrumentAuditRepository.findLastChangeRevision(uuid);
    }

    @Override
    public Revision<Integer, Instrument> findRevision(UUID uuid, Integer revision) {
        return instrumentAuditRepository.findRevision(uuid, revision);
    }

    @Override
    public Page<Revision<Integer, Instrument>> findRevisions(UUID uuid, Pageable pageable) {
        return instrumentAuditRepository.findRevisions(uuid, pageable);
    }

    @Override
    public Page<Revision<Integer, Instrument>> findRevisionByIdAndChangeKindNotIn(UUID id, Collection<AbstractEntityAudit.ChangeKind> changeKinds, Pageable pageable) {
        int skip = pageable.getOffset();
        int limit = pageable.getPageSize();
        return new PageImpl<>(
                instrumentAuditRepository.findRevisions(id).getContent().stream()
                        .filter(f -> !changeKinds.contains(f.getEntity().getChangeKind()))
                        .skip(skip)
                        .limit(limit)
                        .collect(Collectors.toList())
        );
    }

}
