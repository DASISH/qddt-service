package no.nsd.qddt.domain.questionitem;

import no.nsd.qddt.domain.concept.ConceptService;
import no.nsd.qddt.domain.elementref.ElementLoader;
import no.nsd.qddt.domain.parentref.ConceptRef;
import no.nsd.qddt.domain.questionitem.audit.QuestionItemAuditService;
import no.nsd.qddt.domain.responsedomain.ResponseDomain;
import no.nsd.qddt.domain.responsedomain.ResponseDomainService;
import no.nsd.qddt.domain.responsedomain.ResponseKind;
import no.nsd.qddt.domain.responsedomain.audit.ResponseDomainAuditService;
import no.nsd.qddt.exception.ResourceNotFoundException;
import no.nsd.qddt.exception.StackTraceFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static no.nsd.qddt.utils.FilterTool.defaultOrModifiedSort;
import static no.nsd.qddt.utils.FilterTool.defaultSort;
import static no.nsd.qddt.utils.StringTool.IsNullOrTrimEmpty;
import static no.nsd.qddt.utils.StringTool.likeify;

/**
 * @author Stig Norland
 */
@Service("questionItemService")
class QuestionItemServiceImpl implements QuestionItemService {

    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private final QuestionItemRepository questionItemRepository;
    private final QuestionItemAuditService auditService;
    private final ConceptService conceptService;
    private final ResponseDomainService responseDomainService;
    private final ElementLoader<ResponseDomain> rdLoader;

    @Autowired
    public QuestionItemServiceImpl(QuestionItemRepository questionItemRepository,
                                   ResponseDomainAuditService responseDomainAuditService,
                                   ConceptService conceptService,
                                   QuestionItemAuditService questionItemAuditService,
                                   ResponseDomainService responseDomainService) {
        this.questionItemRepository = questionItemRepository;
        this.auditService = questionItemAuditService;
        this.conceptService = conceptService;
        this.responseDomainService = responseDomainService;
        this.rdLoader =  new ElementLoader<>( responseDomainAuditService );
    }

    @Override
    public long count() {
        return questionItemRepository.count();
    }

    @Override
    public boolean exists(UUID uuid) {
        return questionItemRepository.exists(uuid);
    }

    @Override
    public QuestionItem findOne(UUID uuid) {
        return  postLoadProcessing(questionItemRepository.findById(uuid).orElseThrow(
                () -> new ResourceNotFoundException(uuid, QuestionItem.class))

        );
    }

    @Override
    @Transactional()
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR') and hasPermission(#instance,'AGENCY')")
    public QuestionItem save(QuestionItem instance) {
        try {
            return postLoadProcessing(
                questionItemRepository.save(
                    prePersistProcessing(instance)));
        } catch (Exception ex){
            LOG.error("QI save ->",ex);
            throw ex;
        }
    }
//
//    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR')")
//    public List<QuestionItem> save(List<QuestionItem> instances) {
//        return instances.stream().map(this::save).collect(Collectors.toList());
//    }

    @Override
    public void delete(UUID uuid) {
        delete(questionItemRepository.getOne( uuid ));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR') and hasPermission(#instance,'AGENCY')")
    public void delete(QuestionItem instance) {
        try {
            // TODO fix auto delete when mixed responsedomiain are reused.
            if (instance.getResponseDomainRef().getElementId()!= null) {
                ResponseDomain rd = responseDomainService.findOne( instance.getResponseDomainRef().getElementId() );
                if (rd.getResponseKind() == ResponseKind.MIXED) {
                    responseDomainService.delete( instance.getResponseDomainRef().getElementId());
                }
            }
            questionItemRepository.delete(instance);
        } catch (Exception ex){
            LOG.error("QI delete ->",ex);
            throw ex;
        }
    }


    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR')")
    public void delete(List<QuestionItem> instances) {
        questionItemRepository.delete(instances);
    }


    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR','ROLE_CONCEPT','ROLE_VIEW')")
    public Page<QuestionItem> findAllPageable(Pageable pageable){
        try {
            return questionItemRepository.findAll(
                    defaultSort(pageable,"name"));
//                    .map(this::postLoadProcessing);
        } catch (Exception ex) {
            LOG.error("QI catch & continue ->",ex);
            return new PageImpl<>(null);
        }
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR','ROLE_CONCEPT','ROLE_VIEW')")
    public Page<QuestionItem> findByNameOrQuestionOrResponseName(String name, String question, String responseName, String xmlLang, Pageable pageable) {
        pageable = defaultOrModifiedSort(pageable, "name ASC", "updated DESC");
        if (IsNullOrTrimEmpty(name)  &&  IsNullOrTrimEmpty(responseName) && IsNullOrTrimEmpty(question)) {
            name = "%";
        }
        return questionItemRepository.findByNames( likeify(name),likeify(question),likeify(responseName),likeify(xmlLang),pageable );
    }



    /*
    post fetch processing, some elements are not supported by the framework (enver mixed with jpa db queries)
    thus we need to populate some elements ourselves.
    */

    private QuestionItem postLoadProcessing(QuestionItem instance){
        LOG.info( "POST LOAD" );
        try{
            if(instance.getResponseDomainRef().getElementId()!= null && instance.getResponseDomainRef().getElement() == null) {
                rdLoader.fill( instance.getResponseDomainRef() );
            } else {
                LOG.info( "no RD in this QI" );
            }
            instance.setConceptRefs(conceptService.findByQuestionItem(instance.getId(),null).stream()
                .map( ConceptRef::new )
                .collect( Collectors.toList()) );
        } catch (Exception ex){
            StackTraceFilter.println(ex.getStackTrace());
            System.out.println(ex.getMessage());
        }
        return instance;
    }


    private QuestionItem prePersistProcessing(QuestionItem instance){
        LOG.info( "PRE PERSIST" );

        Integer rev = null;
        if(instance.isBasedOn()) {
            LOG.info( "PRE PERSIST -> BASED ON" );
            rev =   (instance.getBasedOnRevision() != null) ? instance.getBasedOnRevision() : auditService.findLastChange( instance.getId() ).getRevisionNumber();
        }
        if (instance.isBasedOn() || instance.isNewCopy()) {
            LOG.info( "PRE PERSIST -> MAKE A COPY" );
            instance = new QuestionItemFactory().copy( instance, rev );
        }
         return instance;
    }
}
