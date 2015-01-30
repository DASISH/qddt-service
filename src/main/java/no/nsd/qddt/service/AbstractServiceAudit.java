package no.nsd.qddt.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;

import java.util.UUID;

/**
 * @author Stig Norland
 */
public interface AbstractServiceAudit<T> extends AbstractService<T> {

    public T findById(UUID id);

    public Revision<Integer, T> findLastChange(Long id);

    public Page<Revision<Integer, T>> findAllRevisionsPageable(Long id, Pageable pageable);

}
