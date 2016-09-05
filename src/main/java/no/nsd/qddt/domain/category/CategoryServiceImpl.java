package no.nsd.qddt.domain.category;

import no.nsd.qddt.domain.HierarchyLevel;
import no.nsd.qddt.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * http://www.ddialliance.org/Specification/DDI-Lifecycle/3.2/XMLSchema/FieldLevelDocumentation/schemas/logicalproduct_xsd/elements/Category.html
 * @author Dag Østgulen Heradstveit
 * @author Stig Norland
 */
@Service("categoryService")
class CategoryServiceImpl implements CategoryService {

    private CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Page<Category> findByNameLike(String name, Pageable pageable) {
        return categoryRepository.findByNameIgnoreCaseLike(name,pageable);
    }

    /*
    This call will give you all Category with HierarchyLevel,CategoryType and
     Name (support whildcard searches)
     */
    @Override
    public Page<Category> findByHierarchyAndCategoryAndNameLike(HierarchyLevel hierarchyLevel, CategoryType categoryType, String name, Pageable pageable) {
        return categoryRepository.findByHierarchyLevelAndCategoryTypeAndNameIgnoreCaseLike(hierarchyLevel,categoryType,name,pageable);
    }


    /*
        This call will give you all Category with CategoryType and Name (support whildcard searches)
        regardless of HierarchyLevel.
         */
    @Override
    public Page<Category> findByCategoryTypeAndNameLike(CategoryType categoryType, String name, Pageable pageable) {
        return categoryRepository.findByCategoryTypeAndNameIgnoreCaseLike(categoryType, name, pageable);
    }

    @Override
    public Page<Category> findByHierarchyAndNameLike(HierarchyLevel level, String name, Pageable pageable) {
        return categoryRepository.findByHierarchyLevelAndNameIgnoreCaseLike(level, name, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return categoryRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean exists(UUID uuid) {
        return categoryRepository.exists(uuid);
    }

    @Override
    @Transactional(readOnly = true)
    public Category findOne(UUID uuid) {
        return categoryRepository.findById(uuid).orElseThrow(
                () -> new ResourceNotFoundException(uuid, Category.class)
        );
    }


    @Override
    @Transactional(readOnly = false)
    public Category save(Category instance) {
        // Category Save fails when there is a mix of new and existing children attached to a new element.
        // This code fixes that.
        Category retval = null;
        try {
            List<Category> tmplist = new ArrayList<>();
            int size = instance.getChildren().size();
            instance.getChildren().removeIf(child -> child.getName() == null  && child.getLabel() == null);
            if (size != instance.getChildren().size())
                System.out.println("removed empty children ->" + (instance.getChildren().size()- size) );
            instance.getChildren().forEach(child -> {tmplist.add(save(child));
                System.out.println(child.getName() + " saved ");});
            instance.setChildren(tmplist);
            if (instance.getId() != null){
                Category fromRepository = findOne(instance.getId());
                if (!instance.fieldCompare(fromRepository))
                    retval= categoryRepository.save(instance);
                else {
                    System.out.println("from repository....");
                    retval = fromRepository;
                }
            }
            else
                retval= categoryRepository.save(instance);

        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return retval;
    }


    @Override
    @Transactional(readOnly = false)
    public List<Category> save(List<Category> instances) {
        return categoryRepository.save(instances);
    }


    @Override
    public void delete(UUID uuid) {
        categoryRepository.delete(uuid);
    }


    @Override
    public void delete(List<Category> instances) {
        categoryRepository.delete(instances);
    }
}
