package no.nsd.qddt.domain.category.audit;

import java.util.Collection;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.category.Category;

/**
 * @author Dag Østgulen Heradstveit
 */
@Repository
interface CategoryAuditRepository extends RevisionRepository<Category, UUID, Integer> {

    //    @Query(value= "select distinct * from concept_aud " +
//            "where id = :id and major = :major and minor = :minor " +
//            "order by rev asc limit 1", nativeQuery = true)
//    Revision<Integer,Category> findVersion(@Param("id") UUID id, @Param("major") int major,@Param("minor") int minor);
    // @JsonView(View.Audit.class)
    Page<Revision<Integer,Category>> findRevisionsByIdAndChangeKindNotIn(UUID uuid, Collection<AbstractEntityAudit.ChangeKind> changeKinds, Pageable pageable);

}
