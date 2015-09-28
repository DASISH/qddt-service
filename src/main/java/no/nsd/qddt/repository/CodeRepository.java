package no.nsd.qddt.repository;

import no.nsd.qddt.domain.response.Code;
import org.springframework.data.envers.repository.support.EnversRevisionRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 * @author Stig Norland
 */
@Repository
public interface CodeRepository extends BaseRepository<Code,UUID>, EnversRevisionRepository<Code, UUID, Integer> {

    List<Code> findByNameIgnoreCaseContains(String tags);

//    @Query("select distinct name from Code")
//    List<String> findDistinctByName();

}
