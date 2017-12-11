package no.nsd.qddt.domain;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.history.RevisionRepository;


/**
 * Interface for generic CRUD operations on a repository for a specific type.
 *
 * @author Dag Østgulen Heradstveit
 * @author Stig Norland
 */
@NoRepositoryBean
public interface BaseRepository<T, ID> extends RevisionRepository<T, ID, Long>, PagingAndSortingRepository<T, ID> {

//    Optional<T> findById(ID id);

}


