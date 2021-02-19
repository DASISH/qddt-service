package no.nsd.qddt.domain.category;

import no.nsd.qddt.domain.AbstractAuditFilter;
import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.classes.exception.InvalidObjectException;
import no.nsd.qddt.domain.classes.exception.ResourceNotFoundException;
import no.nsd.qddt.domain.classes.interfaces.BaseServiceAudit;
import no.nsd.qddt.domain.responsedomain.Code;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static no.nsd.qddt.utils.FilterTool.defaultOrModifiedSort;
import static no.nsd.qddt.utils.StringTool.IsNullOrTrimEmpty;

/**
 * http://www.ddialliance.org/Specification/DDI-Lifecycle/3.2/XMLSchema/FieldLevelDocumentation/schemas/logicalproduct_xsd/elements/Category.html
 * @author Dag Østgulen Heradstveit
 * @author Stig Norland
 */
@Service
public
class CategoryService extends AbstractAuditFilter<Integer,Category> implements BaseServiceAudit<Category,UUID,Integer> {

    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private final CategoryRepository repository;
    private boolean showPrivateComments;

    @Autowired
    CategoryService(CategoryRepository repository) {
        this.repository = repository;
    }

    public Page<Category> findBy(String level, String type, String name, String description, String xmlLang, Pageable pageable) {

        if (IsNullOrTrimEmpty(name)  &&  IsNullOrTrimEmpty(description) ) {
            name = "%";
        }

        CategoryType categoryType =  CategoryType.getEnum(type);
        HierarchyLevel hierarchyLevel = HierarchyLevel.getEnum(level);

        if (categoryType == null && hierarchyLevel == null)
            throw  new IllegalArgumentException( "categoryType OR hierarchyLevel has to be specified." );

        PageRequest sort = defaultOrModifiedSort( pageable, "name ASC", "updated DESC" );

        return  null; //repository.findByQuery(  type, level, likeify(name), likeify(description), likeify( xmlLang ), sort);

    }


    @Transactional(readOnly = true)
    public long count() {
        return repository.count();
    }

    @Transactional(readOnly = true)
    public boolean exists(UUID id) {
        return repository.existsById(id);
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR','ROLE_CONCEPT','ROLE_VIEW')")
    public Category findOne(UUID id) {
        return postLoadProcessing(repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(id, Category.class))
        );
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR') and hasPermission(#instance,'AGENCY')")
    public Category save(Category instance) {
        return postLoadProcessing( 
            repository.save(
                prePersistProcessing(instance)));
    }


    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR')")
    public void delete(UUID id) {
        repository.deleteById(id);
    }


    private final List<Code> _codes = new ArrayList<>( 0 );

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
            throw e;
        }
    }

    private Category postLoadProcessing(Category instance) {
        instance.setCodes(_codes);
        _codes.clear();
        return instance;
    }

    @Override
    public Revision<Integer, Category> findLastChange(UUID id) {
        return postLoadProcessing(repository.findLastChangeRevision(id).orElseThrow( org.springframework.data.rest.webmvc.ResourceNotFoundException::new ));
    }


    @Override
    public Revision<Integer, Category> findRevision(UUID id, Integer revision) {
        return postLoadProcessing(repository.findRevision(id, revision).orElseThrow( org.springframework.data.rest.webmvc.ResourceNotFoundException::new ));
    }

    @Override
    public Page<Revision<Integer, Category>> findRevisions(UUID id, Pageable pageable) {
        return repository.findRevisions(id, pageable).
            map(this::postLoadProcessing);
    }

    @Override
    public Revision<Integer, Category> findFirstChange(UUID id) {
        return postLoadProcessing(repository.findRevisions(id).
            getContent().get(0));
    }

    public Page<Revision<Integer, Category>> findRevisionsByChangeKindIncludeLatest(UUID id, Collection<AbstractEntityAudit.ChangeKind> changeKinds, Pageable pageable) {
        return getPageIncLatest(repository.findRevisions(id),changeKinds,pageable);
    }

    protected Revision<Integer, Category> postLoadProcessing(Revision<Integer, Category> instance) {
        assert  (instance != null);
        instance.getEntity().getVersion().setRevision( instance.getRevisionNumber().orElseThrow( org.springframework.data.rest.webmvc.ResourceNotFoundException::new ) );
        instance.getEntity().setCodes(_codes);
        _codes.clear();
        return Revision.of( instance.getMetadata(),postLoadProcessing(instance.getEntity()));
    }

    @Override
    public @NotNull void setShowPrivateComment(boolean showPrivate) {
        showPrivateComments=showPrivate;
    }


}
