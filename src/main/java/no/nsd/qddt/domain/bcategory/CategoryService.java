package no.nsd.qddt.domain.bcategory;

import no.nsd.qddt.domain.BaseService;
import no.nsd.qddt.domain.HierarchyLevel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
public interface CategoryService extends BaseService<Category, UUID> {

    List<Category>findByNameLike(String name);

    Page<Category>findByNamePageable(String name, Pageable pageable);

    Page<Category>findByCategoryTypeAndNameLike(CategoryType categoryType,String name,Pageable pageable );

    Page<Category>findRootLevelByName(String name,Pageable pageable );

    Page<Category>findGroupByName(String name,Pageable pageable );

    Page<Category>findByHierarchyAndCategoryAndName(HierarchyLevel hierarchyLevel, CategoryType categoryType,String name,Pageable pageable);

//    public List<String> findAllCategoies();
//
//    public Page<String> findAllCategoies(Pageable pageable);
}
