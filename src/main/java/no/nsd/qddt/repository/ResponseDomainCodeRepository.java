package no.nsd.qddt.repository;

import no.nsd.qddt.domain.response.ResponseDomainCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.envers.repository.support.EnversRevisionRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Dag Østgulen Heradstveit
 */
@Repository
public interface ResponseDomainCodeRepository extends EnversRevisionRepository<ResponseDomainCode, Long, Integer> {

    List<ResponseDomainCode> findAll();

    Page<ResponseDomainCode> findAll(Pageable pageable);

    ResponseDomainCode save(ResponseDomainCode instance);

    void delete(ResponseDomainCode instance);

    List<ResponseDomainCode> findByResponseDomainIdOrderByRankAsc(Long responseDomainId);

    List<ResponseDomainCode> findByCodeIdOrderByResponseDomainIdAsc(Long codeId);
}