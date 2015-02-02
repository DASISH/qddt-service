package no.nsd.qddt.service;

import no.nsd.qddt.domain.Attachment;
import no.nsd.qddt.domain.response.ResponseDomain;
import no.nsd.qddt.exception.ResourceNotFoundException;
import no.nsd.qddt.repository.ResponseDomainRepository;
import org.apache.commons.lang.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 * @author Stig Norland
 */
@Service("responseDomainService")
public class ResponseDomainServiceImpl implements ResponseDomainService {

    private ResponseDomainRepository responseDomainRepository;

    @Autowired
    public ResponseDomainServiceImpl(ResponseDomainRepository responseDomainRepository) {
        this.responseDomainRepository = responseDomainRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseDomain findById(Long id) {
        return responseDomainRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(id, ResponseDomain.class)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResponseDomain> findAll() {
        return responseDomainRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ResponseDomain> findAllPageable(Pageable pageable) {
        return responseDomainRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = false)
    public ResponseDomain save(ResponseDomain instance) {

        instance.setCreated(LocalDateTime.now());
        return responseDomainRepository.save(instance);
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(ResponseDomain instance) { responseDomainRepository.delete(instance);  }

    @Override
    @Transactional(readOnly = true)
    public Revision<Integer, ResponseDomain> findLastChange(Long id) {

        return responseDomainRepository.findLastChangeRevision(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Revision<Integer, ResponseDomain> findEntityAtRevision(Long id, Integer revision) {
        return responseDomainRepository.findEntityAtRevision(id, revision);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Revision<Integer, ResponseDomain>> findAllRevisionsPageable(Long id, Pageable pageable) {

        return responseDomainRepository.findRevisions(id,pageable);
    }

}
