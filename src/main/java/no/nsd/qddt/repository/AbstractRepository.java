package no.nsd.qddt.repository;

import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

/**
 * @author Dag Østgulen Heradstveit
 */
@NoRepositoryBean
public interface AbstractRepository<T> {

    Optional<T> findById(Long id);

}
