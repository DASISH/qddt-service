package no.nsd.qddt.repository;

import no.nsd.qddt.domain.HashTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Stig Norland
 */
@Repository
public interface TagRepository extends JpaRepository<HashTag, Long>{

}
