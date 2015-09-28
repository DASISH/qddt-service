package no.nsd.qddt.repository;

import no.nsd.qddt.domain.SurveyProgram;
import org.springframework.data.envers.repository.support.EnversRevisionRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
@Repository
public interface SurveyRepository extends BaseRepository<SurveyProgram,UUID>, EnversRevisionRepository<SurveyProgram, UUID, Integer> {

    //Optional<Survey> findBySuvey(Long id);
}
