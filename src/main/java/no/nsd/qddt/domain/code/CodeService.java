package no.nsd.qddt.domain.code;

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
public interface CodeService extends BaseService<Code, UUID> {

    /**
     * Find ResponseDomainCode by question
     * @param responseDomainId
     * @return
     */
    List<Code> findByResponseDomainId(UUID responseDomainId);

    /**
     * Find ResponseDomainCode by category
     * @param codeId
     * @return
     */
    List<Code> findByCategoryId(UUID codeId);

}