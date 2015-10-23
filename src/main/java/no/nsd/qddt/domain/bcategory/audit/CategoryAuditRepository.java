package no.nsd.qddt.domain.bcategory.audit;

import no.nsd.qddt.domain.bcategory.Category;
import org.springframework.data.envers.repository.support.EnversRevisionRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
@Repository
interface CategoryAuditRepository extends EnversRevisionRepository<Category, UUID, Integer> {

}
