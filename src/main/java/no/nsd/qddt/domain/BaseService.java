package no.nsd.qddt.domain;

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
    <S extends T> S save(S instance);

    /**
     * Store collection og objects Ts to backstore.
     * @param instances collection of objects to store
     * @return collection of saved objects returned from backstore.
     */
    List<T> save(List<T> instances);

    /**
     * Deletes object with id ID from backstore, exception raised by failure.
     * @param id identifier ID
     */
    void delete(ID id);

    /**
     * Deletes object with these IDs from backstore, exception raised by failure.
     * @param instances list of indetifier IDs
     */
    void delete(List<T> instances);




}
