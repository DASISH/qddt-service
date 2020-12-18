package no.nsd.qddt.domain.responsedomain;

import no.nsd.qddt.domain.category.CategoryService;
import no.nsd.qddt.domain.responsedomain.audit.ResponseDomainAuditService;
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

import java.util.UUID;

import static no.nsd.qddt.utils.FilterTool.defaultOrModifiedSort;
import static no.nsd.qddt.utils.StringTool.likeify;

/**
 * @author Dag Østgulen Heradstveit
 * @author Stig Norland
 */
@Service("responseDomainService")
class ResponseDomainServiceImpl implements ResponseDomainService {

    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());
    private final ResponseDomainRepository responseDomainRepository;
    private final ResponseDomainAuditService auditService;
    private final CategoryService categoryService;

    @Autowired
    public ResponseDomainServiceImpl(ResponseDomainRepository responseDomainRepository,
                                     CategoryService categoryService,
                                     ResponseDomainAuditService responseDomainAuditService) {
        this.responseDomainRepository = responseDomainRepository;
        this.categoryService = categoryService;
        this.auditService = responseDomainAuditService;
    }

    @Override
    public long count() {
        return responseDomainRepository.count();
    }

    @Override
    public boolean exists(UUID uuid) {
        return responseDomainRepository.existsById(uuid);
    }

    @Override
    public ResponseDomain findOne(UUID uuid) {
        return responseDomainRepository.findById(uuid).map( this::postLoadProcessing ).orElseThrow(
                () -> new ResourceNotFoundException(uuid, ResponseDomain.class));
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR','ROLE_CONCEPT','ROLE_VIEW')")
    public Page<ResponseDomain> findBy(ResponseKind responseKind, String name, String description, String question, String anchor, String xmlLang, Pageable pageable) {
        if (name.isEmpty()  &&  description.isEmpty() && question.isEmpty() && anchor.isEmpty()) {
            name = "%";
        }
        return  responseDomainRepository.findByQuery(
                responseKind.toString(),
                likeify(name),
                likeify(description),
                likeify(question),
                likeify(anchor),
                likeify( xmlLang ),
                defaultOrModifiedSort(pageable, "name ASC"))
            .map( this::postLoadProcessing );
    }


    @Override
    @Transactional(propagation = Propagation.NEVER)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR') and hasPermission(#instance,'AGENCY')")
    public ResponseDomain save(ResponseDomain instance) {

        LOG.info(Thread.currentThread().getStackTrace().toString());
        return postLoadProcessing(
                responseDomainRepository.save(
                        prePersistProcessing(instance)));
    }



    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR')")
    public void delete(UUID uuid) {
        responseDomainRepository.deleteById(uuid);
    }

    private ResponseDomain prePersistProcessing(ResponseDomain instance) {

        ResponseDomainFactory rdf = new ResponseDomainFactory();
        if (instance.isBasedOn()) {
            Integer rev = auditService.findLastChange( instance.getId() ).getRevisionNumber().get();
            instance = rdf.copy( instance, rev );
        } else if (instance.isNewCopy()) {
            instance = rdf.copy( instance, null );
        }
        // read the codes from the MR, into the RD
        if (instance.getCodes().size() == 0)
            instance.populateCodes();


// TODO fix replicated framework func
        instance.beforeUpdate();
        instance.setManagedRepresentation(
            categoryService.save(
                instance.getManagedRepresentation()));

        return instance;
    }


    private ResponseDomain postLoadProcessing(ResponseDomain instance) {
        instance.setChangeComment( null );
        return instance;
    }


}
