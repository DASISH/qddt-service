package no.nsd.qddt.domain.controlconstruct;

import no.nsd.qddt.domain.controlconstruct.audit.ControlConstructAuditService;
import no.nsd.qddt.domain.instruction.InstructionService;
import no.nsd.qddt.domain.questionItem.QuestionItem;
import no.nsd.qddt.domain.questionItem.audit.QuestionItemAuditService;
import no.nsd.qddt.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static no.nsd.qddt.utils.FilterTool.controlConstructSort;

/**
 * @author Dag Østgulen Heradstveit
 * @author Stig Norland
 */
@Service("instrumentQuestionService")
class ControlConstructServiceImpl implements ControlConstructService {

    private ControlConstructRepository controlConstructRepository;
    private ControlConstructAuditService auditService;
    private InstructionService iService;
    private QuestionItemAuditService qiAuditService;
//    private QuestionItemService  qiService;


    @Autowired
    public ControlConstructServiceImpl(ControlConstructRepository ccRepository,
                                       ControlConstructAuditService controlConstructAuditService,
                                       InstructionService iService,
                                       QuestionItemAuditService questionAuditService
//                                        ,QuestionItemService questionItemService
    ) {
        this.controlConstructRepository = ccRepository;
        this.auditService = controlConstructAuditService;
        this.iService = iService;
        this.qiAuditService = questionAuditService;
//        this.qiService = questionItemService;
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
    public ControlConstruct findOne(UUID id) {

        ControlConstruct instance = controlConstructRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id, ControlConstruct.class));

        return postLoadProcessing(instance);
    }

    @Override
    @Transactional()
    public ControlConstruct save(ControlConstruct instance) {
        return postLoadProcessing(
                controlConstructRepository.save(
                        prePersistProcessing(instance)));
    }

    @Override
    @Transactional()
    public List<ControlConstruct> save(List<ControlConstruct> instances) {
        return  instances.stream().map(this::save).collect(Collectors.toList());
    }

    @Override
    @Transactional()
    public void delete(UUID uuid) {
        controlConstructRepository.delete(uuid);
    }

    @Override
    @Transactional()
    public void delete(List<ControlConstruct> instances) {
        controlConstructRepository.delete(instances);
    }


    public ControlConstruct prePersistProcessing(ControlConstruct instance) {
        instance.populateControlConstructInstructions();

        if(instance.isBasedOn()) {
            Integer rev= auditService.findLastChange(instance.getId()).getRevisionNumber();
            instance.makeNewCopy(rev);
        } else if (instance.isNewCopy()) {
            instance.makeNewCopy(null);
        }

        instance.getControlConstructInstructions().forEach(cqi->{
            if (cqi.getInstruction().getId() == null)
                cqi.setInstruction(iService.save(cqi.getInstruction()));
        });
        return instance;
    }

    public ControlConstruct postLoadProcessing(ControlConstruct instance) {
        assert  (instance != null);
        try{
            // instructions has to unpacked into pre and post instructions
            instance.populateInstructions();

            // before returning fetch correct version of QI...
            if (instance.getQuestionItemUUID() == null)
                instance.setQuestionItemRevision(0);
            else {
                if (instance.getQuestionItemRevision() == null || instance.getQuestionItemRevision() <= 0) {
                    Revision<Integer, QuestionItem> rev = qiAuditService.findLastChange(instance.getQuestionItemUUID());
                    instance.setQuestionItemRevision(rev.getRevisionNumber());
                    instance.setQuestionItem(rev.getEntity());
                } else {
                    QuestionItem qi  =qiAuditService.findRevision(instance.getQuestionItemUUID(), instance.getQuestionItemRevision()).getEntity();
                    instance.setQuestionItem(qi);
                }
            }
        } catch (Exception ex){
            ex.printStackTrace();
            System.out.println(ex.getMessage());
        }

        return instance;
    }

    private ConstructJson mapConstruct(ControlConstruct construct){
        switch (construct.getControlConstructKind()) {
            case QUESTION_CONSTRUCT:
                return new ConstructQuestionJson(construct);
            case STATEMENT_CONSTRUCT:
                return new ConstructStatementJson(construct);
            case CONDITION_CONSTRUCT:
                return new ConstructConditionJson(construct);
            case SEQUENCE_CONSTRUCT:
                return new ConstructSequenceJson(construct);
            default:
                return new ConstructJson(construct);
        }
    }


    @Override
    public List<ConstructJson> findByQuestionItems(List<UUID> questionItemIds) {
        assert (questionItemIds.size() > 0);
        return controlConstructRepository.findByquestionItemUUID(questionItemIds.get(0))
            .stream().map(c-> mapConstruct(postLoadProcessing(c))).collect(Collectors.toList());
    }

    @Override
    public List<ConstructJson> findTop25ByQuestionItemQuestion(String question) {
        question = question.replace("*","%");
        PageRequest pageable = new PageRequest(0,25);

        return controlConstructRepository.findByQuery(
                ControlConstructKind.QUESTION_CONSTRUCT.toString(),
                null,question,question,
                controlConstructSort(pageable,"question ASC","name ASC"))
                .map(qi-> mapConstruct(postLoadProcessing(qi))).getContent();
    }


    @Override
    public Page<ConstructJson> findByNameLikeAndControlConstructKind(String name, ControlConstructKind kind, Pageable pageable) {
        name = name.replace("*","%");
        return controlConstructRepository.findByQuery(
                kind.toString(),name,name,name,
                controlConstructSort(pageable,"name ASC","updated DESC"))
                .map(qi-> mapConstruct(postLoadProcessing(qi)));
    }


}
