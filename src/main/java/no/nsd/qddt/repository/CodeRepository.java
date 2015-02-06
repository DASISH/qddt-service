package no.nsd.qddt.repository;

import no.nsd.qddt.domain.response.Code;
import org.springframework.data.envers.repository.support.EnversRevisionRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Dag Østgulen Heradstveit
 * @author Stig Norland
 */
@Repository
public interface CodeRepository extends BaseRepository<Code>, EnversRevisionRepository<Code, Long, Integer> {

    List<Code> findByNameIgnoreCaseContains(String tags);

//    @Query("select distinct name from Code")
//    List<String> findDistinctByName();

}
