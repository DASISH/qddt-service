package no.nsd.qddt.domain.bcategory;

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

    List<Category> findByNameIgnoreCaseLike(String name);

    Page<Category> findByNameIgnoreCaseLike(String name, Pageable pageable);

    Page<Category> findByCategoryTypeAndNameLike(CategoryType categoryType, String name, Pageable pageable);

    Page<Category> findByHierarchyLevelAndNameLike(HierarchyLevel hierarchyLevel, String name, Pageable pageable);

    Page<Category> findByHierarchyLevelAndCategoryTypeAndNameLike(HierarchyLevel hierarchyLevel, CategoryType categoryType, String name, Pageable pageable);

}