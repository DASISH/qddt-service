package no.nsd.qddt.domain.questionItem;

import no.nsd.qddt.domain.concept.ConceptService;
import no.nsd.qddt.domain.questionItem.audit.QuestionItemAuditService;
import no.nsd.qddt.domain.refclasses.ConceptRef;
import no.nsd.qddt.domain.responsedomain.ResponseDomain;
import no.nsd.qddt.domain.responsedomain.audit.ResponseDomainAuditService;
import no.nsd.qddt.exception.ResourceNotFoundException;
import no.nsd.qddt.exception.StackTraceFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static no.nsd.qddt.utils.FilterTool.defaultSort;

/**
 * @author Stig Norland
 */
@Service("questionItemService")
class QuestionItemServiceImpl implements QuestionItemService {

    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private final QuestionItemRepository questionItemRepository;
    private final ResponseDomainAuditService rdAuditService;
    private final QuestionItemAuditService auditService;
    private final ConceptService conceptService;

    @Autowired
    public QuestionItemServiceImpl(QuestionItemRepository questionItemRepository,
                                   ResponseDomainAuditService responseDomainAuditService,
                                   ConceptService conceptService,
                                   QuestionItemAuditService questionItemAuditService) {
        this.questionItemRepository = questionItemRepository;
        this.rdAuditService = responseDomainAuditService;
        this.auditService = questionItemAuditService;
        this.conceptService = conceptService;
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
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR')")
    public QuestionItem save(QuestionItem instance) {
        try {
            QuestionItem qi =  questionItemRepository.save(
                prePersistProcessing(instance));
            return postLoadProcessing(qi);
        } catch (Exception ex){
            LOG.error("QI save ->",ex);
            throw ex;
        }
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR')")
    public List<QuestionItem> save(List<QuestionItem> instances) {
        return instances.stream().map(this::save).collect(Collectors.toList());
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR')")
    public void delete(UUID uuid) {
        try {
            questionItemRepository.delete(uuid);
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
    public Page<QuestionItem> getHierarchy(Pageable pageable) {
        return  questionItemRepository.findAll(
                defaultSort(pageable,"name", "question"));
//                .map(this::postLoadProcessing);
    }

    @Override
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
    public Page<QuestionItem> findByNameLikeAndQuestionLike(String name, String question, Pageable pageable) {
        question = question.replace("*","%");
        name = name.replace("*","%");

        return questionItemRepository.findByNameLikeIgnoreCaseAndQuestionLikeIgnoreCase(name,question,
                defaultSort(pageable,"name","question"));
//                .map(this::postLoadProcessing);
    }

    @Override
    public Page<QuestionItem> findByNameLikeOrQuestionLike(String searchString, Pageable pageable) {
        searchString = searchString.replace("*","%");
        return questionItemRepository.findByNameLikeIgnoreCaseOrQuestionLikeIgnoreCase(searchString,searchString,
                defaultSort(pageable,"name","question"));
//                .map(this::postLoadProcessing);
    }

    /*
    post fetch processing, some elements are not supported by the framework (enver mixed with jpa db queries)
    thus we need to populate some elements ourselves.
    */

    private QuestionItem postLoadProcessing(QuestionItem instance){
        try{
            if(instance.getResponseDomainUUID() != null) {
                if (instance.getResponseDomainRevision() == null || instance.getResponseDomainRevision() <= 0) {
                    Revision<Integer, ResponseDomain> rev = rdAuditService.findLastChange(instance.getResponseDomainUUID());
                    instance.setResponseDomainRevision(rev.getRevisionNumber());
                    instance.setResponseDomain(rev.getEntity());
                } else {
                    try {
                        ResponseDomain rd = rdAuditService.findRevision(instance.getResponseDomainUUID(), instance.getResponseDomainRevision()).getEntity();
                        instance.setResponseDomain(rd);
                    } catch (Exception ex){
                        LOG.error(ex.getMessage());
                        instance.setResponseDomainRevision(0);
                    }
                }
            }
            else
                instance.setResponseDomainRevision(0);

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

        Integer rev = null;
        if(instance.isBasedOn())
            rev= auditService.findLastChange(instance.getId()).getRevisionNumber();

        if (instance.isBasedOn() || instance.isNewCopy())
            instance = new QuestionItemFactory().copy(instance, rev );


        if (instance.getResponseDomain() != null | instance.getResponseDomainUUID() != null) {
            if (instance.getResponseDomainUUID() == null) {
                instance.setResponseDomainUUID(instance.getResponseDomain().getId());
            }
            if (instance.getResponseDomainRevision() <= 0) {
                try {
                    Revision<Integer, ResponseDomain> revnum = rdAuditService.findLastChange(instance.getResponseDomainUUID());
                    instance.setResponseDomainRevision(revnum.getRevisionNumber());
                } catch (Exception ex) {
                    LOG.error("Set default RevisionNumber failed",ex);
                }
            }
        }
        else {
            instance.setResponseDomainRevision(0);
            LOG.info("no repsonsedomain returned from web");
        }
         return instance;
    }

}