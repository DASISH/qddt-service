package no.nsd.qddt.domain.category;

import no.nsd.qddt.domain.BaseRepository;
import no.nsd.qddt.domain.HierarchyLevel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 * @author Stig Norland
 */
@Repository
interface CategoryRepository extends BaseRepository<Category,UUID> {
//    @NamedNativeQuery(name = "findUniqueCategoryInOrder", query= "select distinct category as name from Code  order by category", resultClass= Category.class)

    Page<Category> findByNameIgnoreCaseLike(String name, Pageable pageable);

    Page<Category> findByCategoryTypeAndNameIgnoreCaseLike(CategoryType categoryType, String name, Pageable pageable);

    Page<Category> findByHierarchyLevelAndNameIgnoreCaseLike(HierarchyLevel hierarchyLevel, String name, Pageable pageable);

    Page<Category> findByHierarchyLevelAndCategoryTypeAndNameIgnoreCaseLike(HierarchyLevel hierarchyLevel, CategoryType categoryType, String name, Pageable pageable);



}