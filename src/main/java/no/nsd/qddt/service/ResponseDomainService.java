package no.nsd.qddt.service;

import no.nsd.qddt.domain.response.ResponseDomain;
import org.springframework.data.domain.Page;
import org.springframework.data.history.Revision;

import java.util.List;

/**
 * @author Dag Østgulen Heradstveit
 */
public interface ResponseDomainService {

    public ResponseDomain findById(Long id);

    public List<ResponseDomain> findAll();

    public Revision<Integer, ResponseDomain> findLastChange(Long id);

    public Page<Revision<Integer, ResponseDomain>> findAllRevisionsPageable(ResponseDomain responseDomain, int min, int max);

}
