package no.nsd.qddt.repository;

import no.nsd.qddt.domain.response.ResponseDomainCode;
import org.springframework.data.envers.repository.support.EnversRevisionRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Dag Østgulen Heradstveit
 */
@Repository
public interface ResponseDomainCodeRepository extends EnversRevisionRepository<ResponseDomainCode, Long, Integer> {

    public List<ResponseDomainCode> findByResponseDomainIdOrderByRankAsc(Long responseDomainId);

    public List<ResponseDomainCode> findByCodeIdOrderByResponseDomainIdAsc(Long codeId);
}