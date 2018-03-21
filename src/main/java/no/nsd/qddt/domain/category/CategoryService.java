package no.nsd.qddt.domain.category;

import no.nsd.qddt.domain.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
public interface CategoryService extends BaseService<Category, UUID> {


    Page<Category>findByNameLike(String name, Pageable pageable);

    Page<Category>findByCategoryTypeAndNameLike(CategoryType categoryType,String name,Pageable pageable );

    Page<Category>findByHierarchyAndNameLike(HierarchyLevel entity, String name, Pageable pageable);

    Page<Category>findByHierarchyAndCategoryAndNameLike(HierarchyLevel hierarchyLevel, CategoryType categoryType,String name,Pageable pageable);

}
