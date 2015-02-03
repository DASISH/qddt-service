package no.nsd.qddt.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
@NoRepositoryBean
public interface BaseRepository<T>  {

    Optional<T> findById(Long id);

    Optional<T> findByGuid(UUID id);

    List<T> findAll();

    Page<T> findAll(Pageable pageable);

    T save(T instance);

    void delete(T instance);


}
