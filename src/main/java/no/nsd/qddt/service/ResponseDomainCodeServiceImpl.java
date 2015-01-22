package no.nsd.qddt.service;

import no.nsd.qddt.domain.response.ResponseDomain;
import no.nsd.qddt.domain.response.ResponseDomainCode;
import no.nsd.qddt.domain.response.ResponseDomainCodeId;
import no.nsd.qddt.repository.ResponseDomainCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.history.Revision;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Dag Østgulen Heradstveit
 */
@Service("ResponseDomainService")
public class ResponseDomainCodeServiceImpl implements ResponseDomainCodeService {

    private ResponseDomainCodeRepository responseDomainCodeRepository;

    @Autowired
    public ResponseDomainCodeServiceImpl(ResponseDomainCodeRepository responseDomainCodeRepository) {
        this.responseDomainCodeRepository = responseDomainCodeRepository;
    }

    @Override
    public List<ResponseDomainCode> findAll() {
        return responseDomainCodeRepository.findAll();
    }

    @Override
    public Revision<Integer, ResponseDomainCode> findLastChange(ResponseDomainCodeId responseDomainCodeId) {
        return responseDomainCodeRepository.findLastChangeRevision(responseDomainCodeId);
    }

    @Override
    public Page<Revision<Integer, ResponseDomainCode>> findAllRevisionsPageable(ResponseDomainCode responseDomainCode, int min, int max) {
        return responseDomainCodeRepository.findRevisions(responseDomainCode.getPk(), new PageRequest(min, max));
    }

    @Override
    public ResponseDomainCode save(ResponseDomainCode responseDomainCode) {
        return responseDomainCodeRepository.save(responseDomainCode);
    }

    @Override
    public ResponseDomain findByPk(ResponseDomainCodeId responseDomainCodeId) {
        return null;
    }

}