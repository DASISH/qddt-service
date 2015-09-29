package no.nsd.qddt.domain.responsedomain;

import no.nsd.qddt.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 * @author Stig Norland
 */
@Service("responseDomainService")
class ResponseDomainServiceImpl implements ResponseDomainService {

    private ResponseDomainRepository responseDomainRepository;

    @Autowired
    public ResponseDomainServiceImpl(ResponseDomainRepository responseDomainRepository) {
        this.responseDomainRepository = responseDomainRepository;
    }


    @Override
    public long count() {
        return responseDomainRepository.count();
    }

    @Override
    public boolean exists(UUID uuid) {
        return responseDomainRepository.exists(uuid);
    }

    @Override
    public ResponseDomain findOne(UUID uuid) {
        return responseDomainRepository.findById(uuid).orElseThrow(
                () -> new ResourceNotFoundException(uuid, ResponseDomain.class)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResponseDomain> findAll() {
        return responseDomainRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ResponseDomain> findAll(Pageable pageable) {
        return responseDomainRepository.findAll(pageable);
    }

    @Override
    public List<ResponseDomain> findAll(Iterable<UUID> uuids) {
        return responseDomainRepository.findAll(uuids);
    }

    @Override
    @Transactional(readOnly = false)
    public ResponseDomain save(ResponseDomain instance) {

        instance.setCreated(LocalDateTime.now());
        return responseDomainRepository.save(instance);
    }

    @Override
    public void delete(UUID uuid) {
        responseDomainRepository.delete(uuid);
    }


    @Override
    public Revision<Integer, ResponseDomain> findLastChange(UUID uuid) {
        return responseDomainRepository.findLastChangeRevision(uuid);
    }

    @Override
    public Revision<Integer, ResponseDomain> findEntityAtRevision(UUID uuid, Integer revision) {
        return responseDomainRepository.findEntityAtRevision(uuid, revision);
    }

    @Override
    public Page<Revision<Integer, ResponseDomain>> findAllRevisionsPageable(UUID uuid, Pageable pageable) {
        return responseDomainRepository.findRevisions(uuid,pageable);
    }
}
