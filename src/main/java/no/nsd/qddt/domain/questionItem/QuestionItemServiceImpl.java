package no.nsd.qddt.domain.questionItem;

import no.nsd.qddt.domain.question.QuestionService;
import no.nsd.qddt.domain.questionItem.audit.QuestionItemAuditService;
import no.nsd.qddt.domain.responsedomain.ResponseDomain;
import no.nsd.qddt.domain.responsedomain.audit.ResponseDomainAuditService;
import no.nsd.qddt.exception.ResourceNotFoundException;
import no.nsd.qddt.exception.StackTraceFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
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


    private final QuestionItemRepository questionItemRepository;
    private final ResponseDomainAuditService rdAuditService;
    private final QuestionItemAuditService auditService;
    private final QuestionService questionService;

    @Autowired
    public QuestionItemServiceImpl(QuestionItemRepository questionItemRepository,
                                   ResponseDomainAuditService responseDomainAuditService,
                                   QuestionService questionService,
                                   QuestionItemAuditService questionItemAuditService) {
        this.questionItemRepository = questionItemRepository;
        this.rdAuditService = responseDomainAuditService;
        this.questionService = questionService;
        this.auditService = questionItemAuditService;
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
    public QuestionItem save(QuestionItem instance) {
        try {
            return postLoadProcessing(
                    questionItemRepository.save(
                            prePersistProcessing(instance)));
        } catch (Exception ex){
            System.out.println("QI save ->"  + ex.getMessage());
            StackTraceFilter.filter(ex.getStackTrace());
            throw ex;
        }
    }

    @Override
    public List<QuestionItem> save(List<QuestionItem> instances) {
        return instances.stream().map(this::save).collect(Collectors.toList());
    }

    @Override
    public void delete(UUID uuid) {
        try {
            System.out.println("delete question " + uuid);
            questionItemRepository.delete(uuid);
        } catch (Exception ex){
            StackTraceFilter.println(ex.getStackTrace());
            throw ex;
        }
    }

    @Override
    public void delete(List<QuestionItem> instances) {
        questionItemRepository.delete(instances);
    }

    @Override
    public Page<QuestionItem> getHierarchy(Pageable pageable) {
        return  questionItemRepository.findAll(
                defaultSort(pageable,"name", "questions.question"))
                .map(this::postLoadProcessing);
    }

    @Override
    public Page<QuestionItem> findAllPageable(Pageable pageable){
        try {
            return questionItemRepository.findAll(
                    defaultSort(pageable,"name"))
                    .map(this::postLoadProcessing);
        }catch (Exception ex){
            StackTraceFilter.println(ex.getStackTrace());
            return new PageImpl<>(null);
        }
    }

    @Override
    public Page<QuestionItem> findByNameLikeAndQuestionLike(String name, String question, Pageable pageable) {
        question = question.replace("*","%");
        name = name.replace("*","%");

        return questionItemRepository.findByNameLikeIgnoreCaseAndQuestionQuestionLikeIgnoreCase(name,question,
                defaultSort(pageable,"name","question.question"))
                .map(this::postLoadProcessing);
    }

    @Override
    public Page<QuestionItem> findByNameLikeOrQuestionLike(String searchString, Pageable pageable) {
        searchString = searchString.replace("*","%");

        return questionItemRepository.findByNameLikeIgnoreCaseOrQuestionQuestionLikeIgnoreCase(searchString,searchString,
                defaultSort(pageable,"name","question.question"))
                .map(this::postLoadProcessing);
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
                    System.out.println("Latest RD fetched " + rev.getRevisionNumber());
                } else {
                    try {
                        ResponseDomain rd = rdAuditService.findRevision(instance.getResponseDomainUUID(), instance.getResponseDomainRevision()).getEntity();
                        instance.setResponseDomain(rd);
                    } catch (Exception ex){
                        System.out.println(ex.getMessage());
                        instance.setResponseDomainRevision(0);
                    }
                }
            }
            else
                instance.setResponseDomainRevision(0);
        } catch (Exception ex){
            StackTraceFilter.println(ex.getStackTrace());
            System.out.println(ex.getMessage());
        }
        return instance;
    }


    private QuestionItem prePersistProcessing(QuestionItem instance){

        if(instance.isBasedOn()) {
            Integer rev= auditService.findLastChange(instance.getId()).getRevisionNumber();
            instance.makeNewCopy(rev);
        } else if (instance.isNewCopy()){
            instance.makeNewCopy(null);
        }

        if (instance.getQuestion().getId() == null)
            instance.setQuestion(questionService.save(instance.getQuestion()));

        if (instance.getResponseDomain() != null | instance.getResponseDomainUUID() != null) {
            if (instance.getResponseDomainUUID() == null) {
                instance.setResponseDomainUUID(instance.getResponseDomain().getId());
            }
            if (instance.getResponseDomainRevision() <= 0) {
                try {
                    Revision<Integer, ResponseDomain> revnum = rdAuditService.findLastChange(instance.getResponseDomainUUID());
                    instance.setResponseDomainRevision(revnum.getRevisionNumber());
                } catch (Exception ex) {
                    System.out.println("Set default RevisionNumber failed");
                    System.out.println(ex.getMessage());
                }
            }
        }
        else {
            instance.setResponseDomainRevision(0);
            System.out.println("no repsonsedomain returned from web");
        }
         return instance;
    }

}