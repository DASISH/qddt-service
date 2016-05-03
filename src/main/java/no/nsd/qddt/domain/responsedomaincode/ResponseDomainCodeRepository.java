package no.nsd.qddt.domain.responsedomaincode;

import no.nsd.qddt.domain.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
@Repository
interface ResponseDomainCodeRepository extends BaseRepository<ResponseDomainCode, UUID> {

    List<ResponseDomainCode> findByQuestionIdOrderByCategoryIndexAsc(UUID questionId);

    List<ResponseDomainCode> findByCategoryIdOrderByCategoryIndexAsc(UUID codeId);
}