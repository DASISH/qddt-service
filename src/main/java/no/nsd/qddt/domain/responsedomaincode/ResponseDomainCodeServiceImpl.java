package no.nsd.qddt.domain.responsedomaincode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 * @author Stig Norland
 */
@Service("responseDomainCodeService")
class ResponseDomainCodeServiceImpl implements ResponseDomainCodeService {

    private ResponseDomainCodeRepository responseDomainCodeRepository;

    @Autowired
    public ResponseDomainCodeServiceImpl(ResponseDomainCodeRepository responseDomainCodeRepository) {
        this.responseDomainCodeRepository = responseDomainCodeRepository;
    }


    @Override
    public long count() {
        return responseDomainCodeRepository.count();
    }

    @Override
    public boolean exists(UUID uuid) {
        return responseDomainCodeRepository.exists(uuid);
    }

    @Override
    public ResponseDomainCode findOne(UUID uuid) {
        return responseDomainCodeRepository.findOne(uuid);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResponseDomainCode> findAll() {
        return responseDomainCodeRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ResponseDomainCode> findAll(Pageable pageable) {

        return responseDomainCodeRepository.findAll(pageable);
    }

    @Override
    public List<ResponseDomainCode> findAll(Iterable<UUID> uuids) {
        return responseDomainCodeRepository.findAll(uuids);
    }


    @Override
    @Transactional(readOnly = false)
    public ResponseDomainCode save(ResponseDomainCode instance) {
        return responseDomainCodeRepository.save(instance);
    }

    @Override
    public void delete(UUID uuid) {
        responseDomainCodeRepository.delete(uuid);
    }

    @Override
    public List<ResponseDomainCode> findByResponseDomainId(UUID responseDomainId) {
        return responseDomainCodeRepository.findByResponseDomainIdOrderByCodeIdxAsc(responseDomainId);
    }

    @Override
    public List<ResponseDomainCode> findByCodeId(UUID codeId) {
        return null;
    }

    @Override
    public Revision<Integer, ResponseDomainCode> findLastChange(UUID uuid) {
        return responseDomainCodeRepository.findLastChangeRevision(uuid);
    }

    @Override
    public Revision<Integer, ResponseDomainCode> findEntityAtRevision(UUID uuid, Integer revision) {
        return responseDomainCodeRepository.findRevision(uuid, revision);
    }

    @Override
    public Page<Revision<Integer, ResponseDomainCode>> findAllRevisionsPageable(UUID uuid, Pageable pageable) {
        return responseDomainCodeRepository.findRevisions(uuid, pageable);
    }
}