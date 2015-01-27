package no.nsd.qddt.service;

import no.nsd.qddt.domain.response.ResponseDomain;
import no.nsd.qddt.repository.ResponseDomainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.history.Revision;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Dag Østgulen Heradstveit
 */
@Service("responseDomainService")
public class ResponseDomainServiceImpl implements ResponseDomainService {

    private ResponseDomainRepository responseDomainRepository;

    @Autowired
    public ResponseDomainServiceImpl(ResponseDomainRepository responseDomainRepository) {
        this.responseDomainRepository = responseDomainRepository;
    }

    @Override
    public ResponseDomain findById(Long id) {
        return responseDomainRepository.findOne(id);
    }

    @Override
    public List<ResponseDomain> findAll() {
        return responseDomainRepository.findAll();
    }

    @Override
    public ResponseDomain save(ResponseDomain responseDomain) {
        responseDomain.setCreated(LocalDateTime.now());
        return responseDomainRepository.save(responseDomain);
    }

    @Override
    public Revision<Integer, ResponseDomain> findLastChange(Long id) {
        return responseDomainRepository.findLastChangeRevision(id);
    }

    @Override
    public Page<Revision<Integer, ResponseDomain>> findAllRevisionsPageable(ResponseDomain responseDomain, int page, int size) {
        return responseDomainRepository.findRevisions(responseDomain.getId(), new PageRequest(page, size));
    }
}
