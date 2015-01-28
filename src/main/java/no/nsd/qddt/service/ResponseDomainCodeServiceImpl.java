package no.nsd.qddt.service;

import no.nsd.qddt.domain.response.ResponseDomainCode;
import no.nsd.qddt.repository.ResponseDomainCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
public class ResponseDomainCodeServiceImpl implements ResponseDomainCodeService {

    private ResponseDomainCodeRepository responseDomainCodeRepository;

    @Autowired
    public ResponseDomainCodeServiceImpl(ResponseDomainCodeRepository responseDomainCodeRepository) {
        this.responseDomainCodeRepository = responseDomainCodeRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseDomainCode findById(Long id) { return responseDomainCodeRepository.findOne(id); }

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
    @Transactional(readOnly = false)
    public ResponseDomainCode save(ResponseDomainCode instance) {

        return responseDomainCodeRepository.save(instance);
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(ResponseDomainCode instance) {

        responseDomainCodeRepository.delete(instance);
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


    @Override
    @Transactional(readOnly = true)
    public ResponseDomainCode findById(UUID id) {

        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public Revision<Integer, ResponseDomainCode> findLastChange(Long id) {

        return responseDomainCodeRepository.findLastChangeRevision(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Revision<Integer, ResponseDomainCode>> findAllRevisionsPageable(Long id, Pageable pageable) {

        return responseDomainCodeRepository.findRevisions(id,pageable);
    }
}