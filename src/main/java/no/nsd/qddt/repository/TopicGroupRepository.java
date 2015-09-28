package no.nsd.qddt.repository;

import no.nsd.qddt.domain.TopicGroup;
import org.springframework.data.envers.repository.support.EnversRevisionRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author Stig Norland
 */
@Repository
public interface TopicGroupRepository extends BaseRepository<TopicGroup,UUID>, EnversRevisionRepository<TopicGroup, UUID, Integer> {}
