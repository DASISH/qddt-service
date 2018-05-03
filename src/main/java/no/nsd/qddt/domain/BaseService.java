package no.nsd.qddt.domain;

import org.springframework.dao.DataAccessException;

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
     * @param id identifier ID
     */
//    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    void delete(ID id) throws DataAccessException;

    /**
     * Deletes object with these IDs from backstore, exception raised by failure.
     * @param instances list of identifier IDs
     */
//    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    void delete(List<T> instances) throws DataAccessException;

}
