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
     * Store object T to backstore
     * @param instance object T
     * @return saved instanse T (may have fields updated by backstore)
     */
    T save(T instance);

    T save(List<T> instances);

    /**
     * Deletes object with id ID from backstore, exception raised by failure.
     * @param id identifier ID
     */
    void delete(ID id);

    void delete(List<T> instances);

    void purge(ID id);

    void purge(List<T> instance);


}
