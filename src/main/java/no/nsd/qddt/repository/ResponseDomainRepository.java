package no.nsd.qddt.repository;

import no.nsd.qddt.domain.response.ResponseDomain;
import org.springframework.data.envers.repository.support.EnversRevisionRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
@Repository
public interface ResponseDomainRepository extends BaseRepository<ResponseDomain,UUID>,EnversRevisionRepository<ResponseDomain, UUID, Integer> {
}
