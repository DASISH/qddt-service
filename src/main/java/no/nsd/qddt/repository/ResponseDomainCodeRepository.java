package no.nsd.qddt.repository;

import no.nsd.qddt.domain.response.ResponseDomainCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.envers.repository.support.EnversRevisionRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
@Repository
public interface ResponseDomainCodeRepository extends BaseRepository<ResponseDomainCode,UUID>, EnversRevisionRepository<ResponseDomainCode, UUID, Integer> {

    List<ResponseDomainCode> findByResponseDomainIdOrderByRankAsc(UUID responseDomainId);

    List<ResponseDomainCode> findByCodeIdOrderByResponseDomainIdAsc(UUID codeId);
}