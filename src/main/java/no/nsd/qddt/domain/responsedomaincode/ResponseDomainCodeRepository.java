package no.nsd.qddt.domain.responsedomaincode;

import no.nsd.qddt.domain.BaseRepository;
import org.springframework.data.envers.repository.support.EnversRevisionRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
@Repository
interface ResponseDomainCodeRepository extends BaseRepository<ResponseDomainCode,UUID>, EnversRevisionRepository<ResponseDomainCode, UUID, Integer> {

    List<ResponseDomainCode> findByResponseDomainIdOrderByCodeIdxAsc(UUID responseDomainId);

    List<ResponseDomainCode> findByCodeIdOrderByResponseDomainIdAsc(UUID codeId);
}