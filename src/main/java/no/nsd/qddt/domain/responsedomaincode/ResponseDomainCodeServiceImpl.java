package no.nsd.qddt.domain.responsedomaincode;

import org.springframework.beans.factory.annotation.Autowired;
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
    @Transactional(readOnly = false)
    public ResponseDomainCode save(ResponseDomainCode instance) {
        return responseDomainCodeRepository.save(instance);
    }

    @Override
    public List<ResponseDomainCode> save(List<ResponseDomainCode> instances) {
        return responseDomainCodeRepository.save(instances);
    }

    @Override
    public void delete(UUID uuid) {
        responseDomainCodeRepository.delete(uuid);
    }

    @Override
    public void delete(List<ResponseDomainCode> instances) {
        responseDomainCodeRepository.delete(instances);
    }

    @Override
    public List<ResponseDomainCode> findByResponseDomainId(UUID responseDomainId) {
        return responseDomainCodeRepository.findByResponseDomainIdOrderByCodeIdxAsc(responseDomainId);
    }

    @Override
    public List<ResponseDomainCode> findByCodeId(UUID codeId) {
        return responseDomainCodeRepository.findByCodeIdOrderByResponseDomainIdAsc(codeId);
    }
}