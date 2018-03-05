package no.nsd.qddt.domain.responsedomain;

import no.nsd.qddt.domain.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
@Repository
interface ResponseDomainRepository extends BaseRepository<ResponseDomain,UUID> {


    Page<ResponseDomain> findByResponseKindAndNameIgnoreCaseLikeAndDescriptionIgnoreCaseLike(ResponseKind responseKind, String name, String description, Pageable pageable);
}
