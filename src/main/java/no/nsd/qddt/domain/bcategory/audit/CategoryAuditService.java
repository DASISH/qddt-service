package no.nsd.qddt.domain.bcategory.audit;

import no.nsd.qddt.domain.BaseServiceAudit;
import no.nsd.qddt.domain.bcategory.Category;

import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
public interface CategoryAuditService extends BaseServiceAudit<Category, UUID, Integer > {

}
