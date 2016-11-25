package no.nsd.qddt.domain.responsedomain;

import no.nsd.qddt.domain.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 * @author Stig Norland
 */
public interface ResponseDomainService extends BaseService<ResponseDomain,UUID> {

    Page<ResponseDomain> findBy(ResponseKind responseKind, String name,String description,  Pageable pageable);

    Page<ResponseDomain> findByQuestion(ResponseKind responseKind, String name,String question,  Pageable pageable);

    ResponseDomain createMixed(UUID rdId, UUID missingId);
}