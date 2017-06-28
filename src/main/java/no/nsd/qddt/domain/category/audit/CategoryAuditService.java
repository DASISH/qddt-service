package no.nsd.qddt.domain.category.audit;

import no.nsd.qddt.domain.BaseServiceAudit;
import no.nsd.qddt.domain.category.Category;

import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
public interface CategoryAuditService extends BaseServiceAudit<Category, UUID, Integer > {

//    Revision<Integer,Category> findVersion(UUID id, String version);
}
