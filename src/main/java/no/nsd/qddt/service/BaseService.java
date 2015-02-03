package no.nsd.qddt.service;

import no.nsd.qddt.domain.Attachment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Stig Norland
 */
public interface BaseService<T> {

    /**
     * Return a entity based on its ID.
     * @param id Long
     * @return Entity
     */
    T findById(Long id);


    /**
     * Return every entity in backstore as a list,
     * may not be appropriate for most classes
     * @return list of all T's
     */
    List<T> findAll();

    /**
     * Return every entity in backstore as a pageable list,
     * @param pageable a pageable object to handle paging.
     * @return pageable list of all T's
     */
    Page<T> findAll(Pageable pageable);

    /**
     * Store object T to backstore
     * @param instance object T
     * @return saved instanse T (may have fields updated by backstore)
     */
    T save(T instance);

    /**
     * Deletes object T from backstore, exception raised by failure.
     * @param instance object T
     */
    void delete(T instance);

}
