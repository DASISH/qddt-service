package no.nsd.qddt.service;

import no.nsd.qddt.domain.response.ResponseDomainCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;

import java.util.List;

/**
 * @author Dag Østgulen Heradstveit
 * @author Stig Norland
 */
public interface ResponseDomainCodeService extends BaseService<ResponseDomainCode> {

    List<ResponseDomainCode> findByResponseDomainId(Long responseDomainId);

    List<ResponseDomainCode> findByCodeId(Long codeId);

    /**
     * Find the latest changed revision.
     * @param id of the entity
     * @return {@link org.springframework.data.history.Revision}
     */
    Revision<Integer,ResponseDomainCode> findLastChange(Long id);

    /**
     * Find the entity based on a revision number.
     * @param id of the entity
     * @param revision number of the entity
     * @return {@link org.springframework.data.history.Revision} at the given revision
     */
    Revision<Integer, ResponseDomainCode> findEntityAtRevision(Long id, Integer revision);

    /**
     * Find all revisions and return in a pageable view
     * @param id of the entity
     * @param pageable from controller method
     * @return {@link org.springframework.data.domain.Page} of the entity
     */
    Page<Revision<Integer, ResponseDomainCode>> findAllRevisionsPageable(Long id, Pageable pageable);

}