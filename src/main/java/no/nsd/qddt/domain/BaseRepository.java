package no.nsd.qddt.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.Optional;


/**
 * Interface for generic CRUD operations on a repository for a specific type.
 *
 * @author Dag Østgulen Heradstveit
 * @author Stig Norland
 */
@NoRepositoryBean
public interface BaseRepository<T, ID extends Serializable> extends JpaRepository<T,ID> {

    Optional<T> findById(ID id);
}

//
//    Optional<T> findById(UUID id);
//
//    Optional<T> findByGuid(UUID id);
//
//    List<T> findAll();
//
//    Page<T> findAll(Pageable pageable);
//
//    T save(T instance);
//
//    void delete(T instance);
//
//
//}
