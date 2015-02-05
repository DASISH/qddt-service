package no.nsd.qddt.controller.audit;

import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpEntity;

/**
 * @author Stig Norland
 */
public interface BaseAuditController<T>  {

    HttpEntity<PagedResources<Revision<Integer, T>>> getAllRevisionsPageable(Long id, Pageable pageable);

    Revision<Integer, T> getEntityAtRevision(Long id, Integer revision);

    Revision<Integer, T> getLastChange(Long id);


}
