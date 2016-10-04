package no.nsd.qddt.domain.category.audit;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.category.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.envers.repository.support.EnversRevisionRepository;
import org.springframework.data.history.Revision;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
@Repository
interface CategoryAuditRepository extends EnversRevisionRepository<Category, UUID, Integer> {


//    @Query(value= "select distinct * from concept_aud " +
//            "where id = :id and major = :major and minor = :minor " +
//            "order by rev asc limit 1", nativeQuery = true)
//    Revision<Integer,Category> findVersion(@Param("id") UUID id, @Param("major") int major,@Param("minor") int minor);

    Page<Revision<Integer,Category>> findRevisionsByIdAndChangeKindNotIn(UUID uuid, Collection<AbstractEntityAudit.ChangeKind> changeKinds, Pageable pageable);

//    Revision<Integer,Category> findByMajorAndMinor(UUID id, Integer major, Integer minor);
}
