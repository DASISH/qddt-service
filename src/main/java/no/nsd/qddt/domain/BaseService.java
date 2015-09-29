package no.nsd.qddt.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author Stig Norland
 */
public interface BaseService<T,ID> {

    /**
     *
     * @return number of elements
     */
    long count();

    /**
     *
     * @param id
     * @return
     */
    boolean exists(ID id);

    /**
     * Return a entity based on its ID.
     * @param id ID
     * @return Entity
     */
    T findOne(ID id);

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
     * Return every entity in backstore as a list,
     * may not be appropriate for most classes
     * @return list of all T's
     */
    List<T> findAll(Iterable<ID> ids);

//    /**
//     * Return every entity in backstore as a pageable list,
//     * @param pageable a pageable object to handle paging.
//     * @return pageable list of all T's
//     */
//    Page<T> findAll(Iterable<ID> ids,Pageable pageable);

    /**
     * Store object T to backstore
     * @param instance object T
     * @return saved instanse T (may have fields updated by backstore)
     */
    T save(T instance);


    /**
     * Deletes object with id ID from backstore, exception raised by failure.
     * @param id identifier ID
     */
    void delete(ID id);


}
