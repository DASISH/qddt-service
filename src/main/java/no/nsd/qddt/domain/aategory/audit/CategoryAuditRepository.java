package no.nsd.qddt.domain.aategory.audit;

import no.nsd.qddt.domain.aategory.Category;
import org.springframework.data.envers.repository.support.EnversRevisionRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
@Repository
interface CategoryAuditRepository extends EnversRevisionRepository<Category, UUID, Integer> {

}
