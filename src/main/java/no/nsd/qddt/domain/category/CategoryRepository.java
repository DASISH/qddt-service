package no.nsd.qddt.domain.category;

import no.nsd.qddt.domain.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 * @author Stig Norland
 */
@Repository
interface CategoryRepository extends BaseRepository<Category,UUID> {

    @Query(value = "SELECT ca.* FROM category ca WHERE ( ca.category_kind ILIKE :categoryType OR ca.hierarchy_level ILIKE :hierarchyLevel ) " +
        "AND ( ca.name ILIKE :name or ca.label ILIKE :name  or ca.description ILIKE :description ) " +
        "ORDER BY ?#{#pageable}"
        ,countQuery = "SELECT count(ca.*) FROM category ca WHERE ( ca.category_kind ILIKE :categoryType OR ca.hierarchy_level ILIKE :hierarchyLevel ) " +
        "AND ( ca.name ILIKE :name  or ca.label ILIKE :name or ca.description ILIKE :description ) "
        + " ORDER BY ?#{#pageable}"
        ,nativeQuery = true)
    Page<Category> findByQuery(@Param("categoryType")String categoryType,
                                     @Param("hierarchyLevel")String hierarchyLevel,
                                     @Param("name")String name,
                                     @Param("description")String description,
                                     Pageable pageable);

}
