package no.nsd.qddt.domain.category;

import no.nsd.qddt.domain.responsedomain.Code;
import no.nsd.qddt.exception.InvalidObjectException;
import no.nsd.qddt.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static no.nsd.qddt.utils.FilterTool.defaultOrModifiedSort;
import static no.nsd.qddt.utils.StringTool.IsNullOrTrimEmpty;
import static no.nsd.qddt.utils.StringTool.likeify;

/**
 * http://www.ddialliance.org/Specification/DDI-Lifecycle/3.2/XMLSchema/FieldLevelDocumentation/schemas/logicalproduct_xsd/elements/Category.html
 * @author Dag Østgulen Heradstveit
 * @author Stig Norland
 */
@Service("categoryService")
class CategoryServiceImpl implements CategoryService {

    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());
    private final CategoryRepository repository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<Category> findBy(String level, String type, String name, String description, Pageable pageable) {

        if (IsNullOrTrimEmpty(name)  &&  IsNullOrTrimEmpty(description) ) {
            name = "%";
        }

        CategoryType categoryType =  CategoryType.getEnum(type);
        HierarchyLevel hierarchyLevel = HierarchyLevel.getEnum(level);

        if (categoryType == null && hierarchyLevel == null)
            throw  new IllegalArgumentException( "categoryType OR hierarchyLevel has to be specified." );

        PageRequest sort = defaultOrModifiedSort( pageable, "name ASC", "updated DESC" );

//        LOG.info( "level:'" + level + "' - type:'" + type + "' -name:'" +  likeify(name) + "' - desc:'" +  likeify(description) + "' - sort:" +  sort.toString());

        return  repository.findByQuery(  type, level, likeify(name), likeify(description), sort);

    }


    @Override
    @Transactional(readOnly = true)
    public long count() {
        return repository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean exists(UUID uuid) {
        return repository.exists(uuid);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR','ROLE_CONCEPT','ROLE_VIEW')")
    public Category findOne(UUID uuid) {
        return postLoadProcessing(repository.findById(uuid).orElseThrow(
                () -> new ResourceNotFoundException(uuid, Category.class))
        );
    }


    @Override
    @Transactional(propagation = Propagation.NEVER)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR') and hasPermission(#instance,'AGENCY')")
    public Category save(Category instance) {
        if (_codes.size() >0)
            LOG.error( "_codes not intilaized empty" );
        return postLoadProcessing( 
            repository.save(
                prePersistProcessing(instance)));
    }


    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR')")
    public void delete(UUID uuid) {
        repository.delete(uuid);
    }


    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR')")
    public void delete(List<Category> instances) {
        repository.delete(instances);
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
                instance = repository.save( instance );
                instance.setCode( c );
            }
            return instance;
        }catch (Exception e) {
            LOG.error(e.getClass().getName(), e );
//            System.out.println(e.getClass().getName() + '-' +  e.getMessage());
            throw e;
        }
    }

    private Category postLoadProcessing(Category instance) {
        instance.setCodes(_codes);
        _codes.clear();
        return instance;
    }

}
