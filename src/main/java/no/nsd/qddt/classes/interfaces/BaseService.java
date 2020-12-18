package no.nsd.qddt.classes.interfaces;

import org.springframework.dao.DataAccessException;

/**
 * @author Stig Norland
 */
public interface BaseService<T,ID> {

    /**
     *
     * @return number of entities
     */
    long count();

    /**
     *
     * @param id ID of entity
     * @return true if exists
     */
    boolean exists(ID id);

    /**
     * Return a entity based on its ID.
     * @param id ID of entity
     * @return Entity
     */
    <S extends T> S findOne(ID id);

    /**
     * Store object T to backstore
     * @param instance object T
     * @return saved instance T (may have fields updated by backstore)
     */
//    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR')")
    <S extends T> S save(S instance);


    /**
     * Deletes object with id ID from backstore, exception raised by failure.
     * @param id ID of entity
     */
//    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    void delete(ID id) throws DataAccessException;

    /**
     * Deletes object with these IDs from backstore, exception raised by failure.
     * @param instances list of entity IDs
     */
//    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
//    void delete(List<T> instances) throws DataAccessException;

}
