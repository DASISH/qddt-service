package no.nsd.qddt.service;

import no.nsd.qddt.domain.response.ResponseDomain;
import no.nsd.qddt.domain.response.ResponseDomainCode;
import no.nsd.qddt.domain.response.ResponseDomainCodeId;
import org.springframework.data.domain.Page;
import org.springframework.data.history.Revision;

import java.util.List;

/**
 * @author Dag Østgulen Heradstveit
 */
public interface ResponseDomainCodeService {

    public List<ResponseDomainCode> findAll();

    public Revision<Integer, ResponseDomainCode> findLastChange(ResponseDomainCodeId responseDomainCodeId);

    public Page<Revision<Integer, ResponseDomainCode>> findAllRevisionsPageable(ResponseDomainCode responseDomainCode, int min, int max);

    public ResponseDomainCode save(ResponseDomainCode responseDomainCode);

    public ResponseDomain findByPk(ResponseDomainCodeId responseDomainCodeId);

}