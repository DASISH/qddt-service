package no.nsd.qddt.domain.controlconstruct;

import no.nsd.qddt.domain.controlconstruct.audit.ControlConstructAuditService;
import no.nsd.qddt.domain.controlconstruct.factory.ConditionConstructFactory;
import no.nsd.qddt.domain.controlconstruct.factory.QuestionConstructFactory;
import no.nsd.qddt.domain.controlconstruct.factory.SequenceConstructFactory;
import no.nsd.qddt.domain.controlconstruct.factory.StatementConstructFactory;
import no.nsd.qddt.domain.controlconstruct.json.ConstructJson;
import no.nsd.qddt.domain.controlconstruct.json.ConstructQuestionJson;
import no.nsd.qddt.domain.controlconstruct.pojo.*;
import no.nsd.qddt.domain.instruction.InstructionService;
import no.nsd.qddt.domain.questionItem.QuestionItem;
import no.nsd.qddt.domain.questionItem.audit.QuestionItemAuditService;
import no.nsd.qddt.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static no.nsd.qddt.domain.controlconstruct.json.Converter.mapConstruct;
import static no.nsd.qddt.utils.FilterTool.defaultOrModifiedSort;

/**
 * @author Dag Østgulen Heradstveit
 * @author Stig Norland
 */
@Service("controlConstructService")
class ControlConstructServiceImpl implements ControlConstructService {

    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());
    private final ControlConstructRepository controlConstructRepository;
    private final ControlConstructAuditService auditService;
    private final InstructionService iService;
    private final QuestionItemAuditService qiAuditService;


    @Autowired
    public ControlConstructServiceImpl(ControlConstructRepository ccRepository,
                                       ControlConstructAuditService controlConstructAuditService,
                                       InstructionService iService,
                                       QuestionItemAuditService questionAuditService
    ) {
        this.controlConstructRepository = ccRepository;
        this.auditService = controlConstructAuditService;
        this.iService = iService;
        this.qiAuditService = questionAuditService;
    }

    @Override
    public long count() {
        return controlConstructRepository.count();
    }

    @Override
    public boolean exists(UUID uuid) {
        return controlConstructRepository.exists(uuid);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR','ROLE_CONCEPT','ROLE_VIEW')")
    public ControlConstruct findOne(UUID id) {

        ControlConstruct instance = controlConstructRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id, ControlConstruct.class));

        return postLoadProcessing(instance);
    }

    @Override
    @Transactional()
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR')")
    public  <S extends ControlConstruct> S save(S instance) {
        return postLoadProcessing(
            controlConstructRepository.save(
                prePersistProcessing(instance)));
    }

    @Override
    @Transactional()
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR')")
    public List<ControlConstruct> save(List<ControlConstruct> instances) {
        return  instances.stream().map(this::save).collect(Collectors.toList());
    }

    @Override
    @Transactional()
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR')")
    public void delete(UUID uuid) {
        controlConstructRepository.delete(uuid);
    }

    @Override
    @Transactional()
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR')")
    public void delete(List<ControlConstruct> instances) {
        controlConstructRepository.delete(instances);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR','ROLE_CONCEPT','ROLE_VIEW')")
    public List<ConstructQuestionJson> findByQuestionItems(List<UUID> questionItemIds) {
        assert (questionItemIds.size() > 0);
        return controlConstructRepository.findByquestionItemUUID(questionItemIds.get(0)).stream()
            .map(c-> (ConstructQuestionJson)mapConstruct(postLoadProcessing(c)))
            .collect(Collectors.toList());
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR','ROLE_CONCEPT','ROLE_VIEW')")
    public List<ConstructQuestionJson> findTop25ByQuestionItemQuestion(String question)  {

        question = question.replace("*","%");
        Pageable pageable = defaultOrModifiedSort(new PageRequest(0,25),"question ASC","name ASC");
        return controlConstructRepository
            .findByQuery("QUESTION_CONSTRUCT","%","%",question,pageable)
            .map(qi-> (ConstructQuestionJson)mapConstruct(postLoadProcessing(qi)))
            .getContent();
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR','ROLE_CONCEPT','ROLE_VIEW')")
    public <S extends ConstructJson> Page<S> findByNameLikeAndControlConstructKind(String name, String question, String kind, Pageable pageable) {
        name = name.replace("*","%");
        pageable = defaultOrModifiedSort(pageable, "name ASC", "updated DESC");
        return controlConstructRepository.findByQuery(kind, name, question , question, pageable)
            .map(qi -> mapConstruct(postLoadProcessing(qi)));
    }



    private <S extends ControlConstruct> S  prePersistProcessing(S instance) {
        assert  (instance != null);
        switch (instance.getClassKind()) {
            case "QUESTION_CONSTRUCT":
                if (instance instanceof QuestionConstruct) {
                    ((QuestionConstruct)instance).populateControlConstructInstructions();
                    ((QuestionConstruct)instance).getControlConstructInstructions().forEach(cqi->{
                        if (cqi.getInstruction().getId() == null)
                            cqi.setInstruction(iService.save(cqi.getInstruction()));
                    });

                    if(instance.isBasedOn() || instance.isNewCopy()) {
                        QuestionConstructFactory ccf = new QuestionConstructFactory();
                        Integer rev= auditService.findLastChange(instance.getId()).getRevisionNumber();

//                        instance = ccf.copy((QuestionConstruct)instance, rev );
                    }
                }
                break;
            case "SEQUENCE_CONSTRUCT":
                if (instance instanceof Sequence) {
                    if(instance.isBasedOn() || instance.isNewCopy()) {
                        SequenceConstructFactory ccf= new SequenceConstructFactory();
                        Integer rev= auditService.findLastChange(instance.getId()).getRevisionNumber();
//                        instance = ccf.copy(instance, rev );
                    }
                }
                break;
            case "CONDITION_CONSTRUCT":
                if (instance instanceof ConditionConstruct) {
                    if(instance.isBasedOn() || instance.isNewCopy()) {
                        ConditionConstructFactory ccf= new ConditionConstructFactory();
                        Integer rev= auditService.findLastChange(instance.getId()).getRevisionNumber();
//                        instance =  ccf.copy(instance, rev );
                    }
                }       
                break; 
            case "STATEMENT_CONSTRUCT":
                if (instance instanceof StatementItem) {
                    if(instance.isBasedOn() || instance.isNewCopy()) {
                        StatementConstructFactory ccf= new StatementConstructFactory();
                        Integer rev= auditService.findLastChange(instance.getId()).getRevisionNumber();
//                        instance =  ccf.copy(instance, rev );
                    }   
                }
                break;
        }
        return instance;
    }

    private  <S extends ControlConstruct> S  postLoadProcessing(S instance) {
        assert  (instance != null);
        if ( instance instanceof QuestionConstruct) {
            ((QuestionConstruct)instance).populateInstructions();                // instructions has to be unpacked into pre and post instructions

            if (((QuestionConstruct)instance).getQuestionItemUUID() == null)  {   // before returning fetch correct version of QI...
                ((QuestionConstruct)instance).setQuestionItemRevision(0);
            } else {
                Revision<Integer, QuestionItem> rev = qiAuditService.getQuestionItemLastOrRevision(
                    ((QuestionConstruct)instance).getQuestionItemUUID(),
                    ((QuestionConstruct)instance).getQuestionItemRevision());
                    ((QuestionConstruct)instance).setQuestionItemRevision(rev.getRevisionNumber());
                    ((QuestionConstruct)instance).setQuestionItem(rev.getEntity());
            }
        }
        return instance;
    }

}
