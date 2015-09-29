package no.nsd.qddt.domain.study;

import no.nsd.qddt.domain.BaseRepository;
import org.springframework.data.envers.repository.support.EnversRevisionRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
@Repository
interface StudyRepository extends BaseRepository<Study,UUID>, EnversRevisionRepository<Study, UUID, Integer> {
}
