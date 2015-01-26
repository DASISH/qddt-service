package no.nsd.qddt.repository;

import no.nsd.qddt.domain.response.ResponseDomainCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Dag Østgulen Heradstveit
 */
@Repository
public interface ResponseDomainCodeRepository extends RevisionRepository<ResponseDomainCode, Long, Integer>, JpaRepository<ResponseDomainCode, Long> {

    public List<ResponseDomainCode> findByResponseDomainIdOrderByRankAsc(Long responseDomainId);

    public List<ResponseDomainCode> findByCodeIdOrderByResponseDomainIdAsc(Long codeId);
}