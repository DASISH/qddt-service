package no.nsd.qddt.domain.questionItem;

import no.nsd.qddt.domain.question.Question;
import no.nsd.qddt.domain.question.QuestionService;
import no.nsd.qddt.domain.responsedomain.ResponseDomain;
import no.nsd.qddt.domain.responsedomain.audit.ResponseDomainAuditService;
import no.nsd.qddt.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
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


    private QuestionItemRepository questionItemRepository;
    private ResponseDomainAuditService rdAuditService;
    private QuestionService questionService;

    @Autowired
    public QuestionItemServiceImpl(QuestionItemRepository questionItemRepository,
                                   ResponseDomainAuditService responseDomainAuditService,
                                   QuestionService questionService) {
        this.questionItemRepository = questionItemRepository;
        this.rdAuditService = responseDomainAuditService;
        this.questionService = questionService;
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
        return  setRevisionedResponsedomain(questionItemRepository.findById(uuid).orElseThrow(
                () -> new ResourceNotFoundException(uuid, QuestionItem.class))

        );
    }

    @Override
    @Transactional()
    public QuestionItem save(QuestionItem instance) {
        try {
            return setRevisionedResponsedomain(
                    questionItemRepository.save(
                            setDefaultRevision(instance)));
        } catch (Exception ex){
            System.out.println("QI save ->"  + ex.getMessage());
            ex.printStackTrace();
            throw ex;
        }
    }

    @Override
    public List<QuestionItem> save(List<QuestionItem> instances) {
        return instances.stream().map(this::save).collect(Collectors.toList());
    }

    @Override
    public void delete(UUID uuid) {
        questionItemRepository.delete(uuid);
    }

    @Override
    public void delete(List<QuestionItem> instances) {
        questionItemRepository.delete(instances);
    }

    @Override
    public Page<QuestionItem> getHierarchy(Pageable pageable) {
        return  questionItemRepository.findAll(
                defaultSort(pageable,"name", "questions.question"))
                .map(qi-> setRevisionedResponsedomain(qi));
    }

    @Override
    public Page<QuestionItem> findAllPageable(Pageable pageable){
        try {
            return questionItemRepository.findAll(
                    defaultSort(pageable,"name"))
                    .map(qi -> setRevisionedResponsedomain(qi));
        }catch (Exception ex){
            ex.printStackTrace();
            return new PageImpl<>(null);
        }
    }

    @Override
    public Page<QuestionItem> findByNameLikeAndQuestionLike(String name, String question, Pageable pageable) {
        question = question.replace("*","%");
        name = name.replace("*","%");

        return questionItemRepository.findByNameLikeIgnoreCaseAndQuestionQuestionLikeIgnoreCase(name,question,
                defaultSort(pageable,"name","question.question"))
                .map(qi-> setRevisionedResponsedomain(qi));
    }

    @Override
    public Page<QuestionItem> findByNameLikeOrQuestionLike(String searchString, Pageable pageable) {
        searchString = searchString.replace("*","%");

        return questionItemRepository.findByNameLikeIgnoreCaseOrQuestionQuestionLikeIgnoreCase(searchString,searchString,
                defaultSort(pageable,"name","question.question"))
                .map(qi-> setRevisionedResponsedomain(qi));
    }

    /*
    post fetch processing, some elements are not supported by the framework (enver mixed with jpa db queries)
    thus we need to populate some elements ourselves.
    */
    private QuestionItem setRevisionedResponsedomain(QuestionItem instance){
        try{
            if(instance.getResponseDomainUUID() != null) {
                if (instance.getResponseDomainRevision() == null || instance.getResponseDomainRevision() <= 0) {
                    System.out.println("Fetch latest RD");
                    Revision<Integer, ResponseDomain> rev = rdAuditService.findLastChange(instance.getResponseDomainUUID());
                    instance.setResponseDomainRevision(rev.getRevisionNumber());
                    instance.setResponseDomain(rev.getEntity());
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
            ex.printStackTrace();
            System.out.println(ex.getMessage());
        }
        return instance;
    }

    public List<QuestionItem> setRevisionedResponsedomain(List<QuestionItem>instances) {
        return instances.stream().map(p-> setRevisionedResponsedomain(p)).collect(Collectors.toList());
    }

    protected QuestionItem setDefaultRevision(QuestionItem instance){
        if (instance.getId() == null && instance.getQuestion().getId() != null) {
            // new based on entity needs a new copy of question(text)
            instance.setQuestion(questionService.save(instance.getQuestion().newCopyOf()));
        }

        if (instance.getQuestion().getId() == null)
            instance.setQuestion(
                    questionService.save(
                            instance.getQuestion()));


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