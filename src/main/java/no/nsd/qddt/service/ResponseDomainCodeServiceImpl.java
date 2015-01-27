package no.nsd.qddt.service;

import no.nsd.qddt.domain.response.ResponseDomainCode;
import no.nsd.qddt.repository.ResponseDomainCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.history.Revision;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Dag Østgulen Heradstveit
 */
@Service("responseDomainCodeService")
public class ResponseDomainCodeServiceImpl implements ResponseDomainCodeService {

    private ResponseDomainCodeRepository responseDomainCodeRepository;

    @Autowired
    public ResponseDomainCodeServiceImpl(ResponseDomainCodeRepository responseDomainCodeRepository) {
        this.responseDomainCodeRepository = responseDomainCodeRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResponseDomainCode> findAll() {
        return responseDomainCodeRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Revision<Integer, ResponseDomainCode> findLastChange(Long id) {
        return responseDomainCodeRepository.findLastChangeRevision(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Revision<Integer, ResponseDomainCode>> findAllRevisionsPageable(ResponseDomainCode responseDomainCode, int page, int size) {
        return responseDomainCodeRepository.findRevisions(responseDomainCode.getId(), new PageRequest(page, size));
    }

    @Override
    @Transactional(readOnly = false)
    public ResponseDomainCode save(ResponseDomainCode responseDomainCode) {
        return responseDomainCodeRepository.save(responseDomainCode);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResponseDomainCode> findByResponseDomainId(Long responseDomainId) {
        return responseDomainCodeRepository.findByResponseDomainIdOrderByRankAsc(responseDomainId);

    }

    @Override
    @Transactional(readOnly = true)
    public List<ResponseDomainCode> findByCodeId(Long codeId) {
        return responseDomainCodeRepository.findByCodeIdOrderByResponseDomainIdAsc(codeId);

    }


}