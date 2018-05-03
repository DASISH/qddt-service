package no.nsd.qddt.domain.category;

import no.nsd.qddt.domain.responsedomain.Code;
import no.nsd.qddt.exception.InvalidObjectException;
import no.nsd.qddt.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR','ROLE_CONCEPT','ROLE_VIEW')")
    public Page<Category> findByNameLike(String name, Pageable pageable) {
        return categoryRepository.findByNameIgnoreCaseLike(name,defaultSort(pageable,"name","modified"));
    }

    /*
    This call will give you all Category with HierarchyLevel,CategoryType and
     Name (support whildcard searches)
     */
    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR','ROLE_CONCEPT','ROLE_VIEW')")
    public Page<Category> findByHierarchyAndCategoryAndNameLike(HierarchyLevel hierarchyLevel, CategoryType categoryType, String name, Pageable pageable) {
        return categoryRepository.findByHierarchyLevelAndCategoryTypeAndNameIgnoreCaseLike(hierarchyLevel,categoryType,name,
                defaultSort(pageable,"name","modified"));
    }


    /*
        This call will give you all Category with CategoryType and Name (support whildcard searches)
        regardless of HierarchyLevel.
         */
    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR','ROLE_CONCEPT','ROLE_VIEW')")
    public Page<Category> findByCategoryTypeAndNameLike(CategoryType categoryType, String name, Pageable pageable) {
        return categoryRepository.findByCategoryTypeAndNameIgnoreCaseLike(categoryType, name,
                defaultSort(pageable,"name","modified"));
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR','ROLE_CONCEPT','ROLE_VIEW')")
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
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR','ROLE_CONCEPT','ROLE_VIEW')")
    public Category findOne(UUID uuid) {
        return postLoadProcessing(categoryRepository.findById(uuid).orElseThrow(
                () -> new ResourceNotFoundException(uuid, Category.class))
        );
    }


    @Override
    @Transactional(propagation = Propagation.NEVER)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR')")
    public Category save(Category instance) {
        if (_codes.size() >0)
            LOG.error( "_codes not intilaized empty" );
        return postLoadProcessing( 
            categoryRepository.save(
                prePersistProcessing(instance)));
    }


    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR')")
    public void delete(UUID uuid) {
        categoryRepository.delete(uuid);
    }


    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR')")
    public void delete(List<Category> instances) {
        categoryRepository.delete(instances);
    }

  
    private List<Code> _codes = new ArrayList<>( 0 );

    private Category prePersistProcessing(Category instance) {
        // Category Save fails when there is a mix of new and existing children attached to a new element.
        try {
            if (instance.getId() == null) instance.beforeInsert();
            if (!instance.isValid()) throw new InvalidObjectException(instance);

            if (_codes.size()==0)
                _codes.addAll( instance.getCodes() );

            instance.getChildren().forEach(c-> prePersistProcessing(c));


            if (instance.getId() == null) {
                Code c = instance.getCode();
                instance = categoryRepository.save( instance );
                instance.setCode( c );
            }
            return instance;
        }catch (Exception e) {

            System.out.println(e.getClass().getName() + '-' +  e.getMessage());
            throw e;
        }
    }

    private Category postLoadProcessing(Category instance) {
        instance.setCodes(_codes);
        _codes.clear();
        return instance;
    }

}
