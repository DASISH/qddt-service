package no.nsd.qddt.repository;

import no.nsd.qddt.domain.response.ResponseDomain;
import org.springframework.data.envers.repository.support.EnversRevisionRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Dag Østgulen Heradstveit
 */
@Repository
public interface ResponseDomainRepository extends BaseRepository<ResponseDomain>,
        EnversRevisionRepository<ResponseDomain, Long, Integer> {
}
