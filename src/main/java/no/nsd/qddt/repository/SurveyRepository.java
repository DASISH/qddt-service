package no.nsd.qddt.repository;

import no.nsd.qddt.domain.Survey;
import org.springframework.data.envers.repository.support.EnversRevisionRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Dag Østgulen Heradstveit
 */
@Repository
public interface SurveyRepository extends BaseRepository<Survey>, EnversRevisionRepository<Survey, Long, Integer> {

    //Optional<Survey> findBySuvey(Long id);
}
