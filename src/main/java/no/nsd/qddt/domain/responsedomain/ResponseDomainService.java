package no.nsd.qddt.domain.responsedomain;

import no.nsd.qddt.domain.classes.interfaces.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 * @author Stig Norland
 */
public interface ResponseDomainService extends BaseService<ResponseDomain,UUID> {

    Page<ResponseDomain> findBy(ResponseKind responseKind, String name,String description, String question, String anchor, String xmlLang, Pageable pageable);


//    ResponseDomain createMixed(UUID rdId, UUID missingId);
}
