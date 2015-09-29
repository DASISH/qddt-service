package no.nsd.qddt.domain.responsedomaincode;

import no.nsd.qddt.domain.BaseServiceAudit;

import java.util.List;
import java.util.UUID;

/**
 * ResponseDomainCode doesn't hold information about when and why a change has been done
 * to the hierarchy that it "controls", but never the less it is under version control.
 * So it still needs to implement Revision methods.
 *
 * @author Dag Østgulen Heradstveit
 * @author Stig Norland
 */
public interface ResponseDomainCodeService extends BaseServiceAudit<ResponseDomainCode,UUID> {

    /**
     * Find ResponseDomainCode by responseDomain
     * @param responseDomainId
     * @return
     */
    List<ResponseDomainCode> findByResponseDomainId(UUID responseDomainId);

    /**
     * Find ResponseDomainCode by code
     * @param codeId
     * @return
     */
    List<ResponseDomainCode> findByCodeId(UUID codeId);

//    /**
//     * Find the latest changed revision.
//     * @param id of the entity
//     * @return {@link org.springframework.data.history.Revision}
//     */
//    Revision<Integer,ResponseDomainCode> findLastChange(Long id);
//
//    /**
//     * Find the entity based on a revision number.
//     * @param id of the entity
//     * @param revision number of the entity
//     * @return {@link org.springframework.data.history.Revision} at the given revision
//     */
//    Revision<Integer, ResponseDomainCode> findEntityAtRevision(Long id, Integer revision);
//
//    /**
//     * Find all revisions and return in a pageable view
//     * @param id of the entity
//     * @param pageable from controller method
//     * @return {@link org.springframework.data.domain.Page} of the entity
//     */
//    Page<Revision<Integer, ResponseDomainCode>> findAllRevisionsPageable(Long id, Pageable pageable);

}