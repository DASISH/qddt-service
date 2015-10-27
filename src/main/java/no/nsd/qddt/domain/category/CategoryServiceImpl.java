package no.nsd.qddt.domain.category;

import no.nsd.qddt.domain.HierarchyLevel;
import no.nsd.qddt.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional(readOnly = true)
    public List<Category> findByNameLike(String name) {
        return categoryRepository.findByNameIgnoreCaseLike(name);
    }

    @Override
    public Page<Category> findByNamePageable(String name, Pageable pageable) {
        return categoryRepository.findByNameIgnoreCaseLike(name, pageable);
    }

    @Override
    public Page<Category> findRootLevelByName(String name, Pageable pageable) {
        return categoryRepository.findByHierarchyLevelAndNameLike(HierarchyLevel.ROOT_ENTITY, name, pageable);
    }

    @Override
    public Page<Category> findGroupByName(String name, Pageable pageable) {
        return categoryRepository.findByHierarchyLevelAndNameLike(HierarchyLevel.GROUP_ENTITY, name, pageable);
    }

    @Override
    public Page<Category> findByHierarchyAndCategoryAndName(HierarchyLevel hierarchyLevel, CategoryType categoryType, String name, Pageable pageable) {
        return categoryRepository.findByHierarchyLevelAndCategoryTypeAndNameLike(hierarchyLevel,categoryType,name,pageable);
    }

    @Override
    public Page<Category> findByCategoryTypeAndNameLike(CategoryType categoryType, String name, Pageable pageable) {
        return categoryRepository.findByCategoryTypeAndNameLike(categoryType, name, pageable);
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
        return categoryRepository.save(instance);
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
