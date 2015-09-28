package no.nsd.qddt.repository;

import no.nsd.qddt.domain.Study;
import org.springframework.data.envers.repository.support.EnversRevisionRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
@Repository
public interface StudyRepository extends BaseRepository<Study,UUID>, EnversRevisionRepository<Study, UUID, Integer> {
}
