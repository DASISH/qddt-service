package no.nsd.qddt.domain.category;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 * @author Stig Norland
 */
@Repository
public interface CategoryRepository extends RevisionRepository<Category, UUID, Integer>, JpaRepository<Category, UUID> {


    @Query(value = "SELECT ca.* FROM category ca WHERE ( ca.category_kind ILIKE :categoryType OR ca.hierarchy_level ILIKE :hierarchyLevel ) " +
        "AND ( ca.xml_lang ILIKE :xmlLang AND (ca.name LIKE :name or ca.label ILIKE :name  or ca.description ILIKE :description ) )"
        ,countQuery = "SELECT count(ca.*) FROM category ca WHERE ( ca.category_kind ILIKE :categoryType OR ca.hierarchy_level ILIKE :hierarchyLevel ) " +
        "AND ( ca.xml_lang ILIKE :xmlLang AND (ca.name LIKE :name or ca.label ILIKE :name  or ca.description ILIKE :description ) )"
        ,nativeQuery = true)
    Page<Category> findByQuery(@Param("categoryType")String categoryType,
                               @Param("hierarchyLevel")String hierarchyLevel,
                               @Param("name")String name,
                               @Param("description")String description,
                               @Param("xmlLang")String xmlLang,
                               Pageable pageable);

}
