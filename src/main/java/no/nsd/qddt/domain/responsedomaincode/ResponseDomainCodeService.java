package no.nsd.qddt.domain.responsedomaincode;

import no.nsd.qddt.domain.BaseService;

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
public interface ResponseDomainCodeService extends BaseService<ResponseDomainCode, UUID> {

    /**
     * Find ResponseDomainCode by responseDomain
     * @param responseDomainId
     * @return
     */
    List<ResponseDomainCode> findByResponseDomainId(UUID responseDomainId);

    /**
     * Find ResponseDomainCode by category
     * @param codeId
     * @return
     */
    List<ResponseDomainCode> findByCategoryId(UUID codeId);

}