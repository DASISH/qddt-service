package no.nsd.qddt.service;

import no.nsd.qddt.domain.response.ResponseDomainCode;
import no.nsd.qddt.repository.ResponseDomainCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Dag Østgulen Heradstveit
 */
@Service("ResponseDomainService")
public class ResponseDomainServiceImpl implements ResponseDomainService {

    private ResponseDomainCodeRepository responseDomainCodeRepository;

    @Autowired
    public ResponseDomainServiceImpl(ResponseDomainCodeRepository responseDomainCodeRepository) {
        this.responseDomainCodeRepository = responseDomainCodeRepository;
    }

    @Override
    public List<ResponseDomainCode> findAll() {
        return responseDomainCodeRepository.findAll();
    }
}
