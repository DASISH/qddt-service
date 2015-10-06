package no.nsd.qddt.domain.responsedomain;

import no.nsd.qddt.domain.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
@Repository
interface ResponseDomainRepository extends BaseRepository<ResponseDomain,UUID> {

}
