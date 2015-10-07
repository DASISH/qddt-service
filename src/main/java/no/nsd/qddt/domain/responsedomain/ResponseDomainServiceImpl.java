package no.nsd.qddt.domain.responsedomain;

import no.nsd.qddt.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    @Transactional(readOnly = false)
    public ResponseDomain save(ResponseDomain instance) {

        instance.setCreated(LocalDateTime.now());
        return responseDomainRepository.save(instance);
    }

    @Override
    public List<ResponseDomain> save(List<ResponseDomain> instances) {
        return responseDomainRepository.save(instances);
    }

    @Override
    public void delete(UUID uuid) {
        responseDomainRepository.delete(uuid);
    }

    @Override
    public void delete(List<ResponseDomain> instances) {
        responseDomainRepository.delete(instances);
    }
}
