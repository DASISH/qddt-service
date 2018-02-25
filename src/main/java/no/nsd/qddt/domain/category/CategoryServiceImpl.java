package no.nsd.qddt.domain.category;

import no.nsd.qddt.domain.HierarchyLevel;
import no.nsd.qddt.domain.responsedomain.Code;
import no.nsd.qddt.exception.InvalidObjectException;
import no.nsd.qddt.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static no.nsd.qddt.utils.FilterTool.defaultSort;

/**
 * http://www.ddialliance.org/Specification/DDI-Lifecycle/3.2/XMLSchema/FieldLevelDocumentation/schemas/logicalproduct_xsd/elements/Category.html
 * @author Dag Østgulen Heradstveit
 * @author Stig Norland
 */
@Service("categoryService")
class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Page<Category> findByNameLike(String name, Pageable pageable) {
        return categoryRepository.findByNameIgnoreCaseLike(name,defaultSort(pageable,"name","modified"));
    }

    /*
    This call will give you all Category with HierarchyLevel,CategoryType and
     Name (support whildcard searches)
     */
    @Override
    public Page<Category> findByHierarchyAndCategoryAndNameLike(HierarchyLevel hierarchyLevel, CategoryType categoryType, String name, Pageable pageable) {
        return categoryRepository.findByHierarchyLevelAndCategoryTypeAndNameIgnoreCaseLike(hierarchyLevel,categoryType,name,
                defaultSort(pageable,"name","modified"));
    }


    /*
        This call will give you all Category with CategoryType and Name (support whildcard searches)
        regardless of HierarchyLevel.
         */
    @Override
    public Page<Category> findByCategoryTypeAndNameLike(CategoryType categoryType, String name, Pageable pageable) {
        return categoryRepository.findByCategoryTypeAndNameIgnoreCaseLike(categoryType, name,
                defaultSort(pageable,"name","modified"));
    }

    @Override
    public Page<Category> findByHierarchyAndNameLike(HierarchyLevel level, String name, Pageable pageable) {
        return categoryRepository.findByHierarchyLevelAndNameIgnoreCaseLike(level, name,
                defaultSort(pageable,"name","modified"));
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
    @SuppressWarnings("unchecked")
    @Transactional()
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_SUPER','ROLE_USER')")
    public Category save(Category instance) {
        try {
            return prePersistProcessing(instance,false);
        } catch ( Exception ex ) {
            System.out.println(ex.getMessage());
            throw ex;
        }
    }


    @Override
    @Transactional()
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_SUPER','ROLE_USER')")
    public List<Category> save(List<Category> instances) {
        return categoryRepository.save(instances);
    }


    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_SUPER')")
    public void delete(UUID uuid) {
        categoryRepository.delete(uuid);
    }


    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_SUPER')")
    public void delete(List<Category> instances) {
        categoryRepository.delete(instances);
    }

    private Category prePersistProcessing(Category instance, Boolean hasparent) {
        // Category Save fails when there is a mix of new and existing children attached to a new element.
        // This code fixes that.
        try {
            instance.getChildren().forEach(c-> prePersistProcessing(c, true));

            if (!instance.isValid()) throw new InvalidObjectException(instance);

            Code c =  instance.getCode();
            if ( hasparent != true || (instance.getId() == null ) || instance.getHierarchyLevel() == HierarchyLevel.GROUP_ENTITY ) {
                System.out.println("Saving category...");
                instance = categoryRepository.save(instance);
                System.out.println("Saved " + instance.getName() );
            }
            instance.setCode(c);
        } catch (Exception e) {
            System.out.println(e.getClass().getName() + '-' +  e.getMessage());
            throw e;
        }
        System.out.println(instance.toString());
        
        return instance;
    }

/*     private Category postLoadProcessing(Code code, Category instance) {
        instance.setCode(code);
        return instance;
    } */

}
