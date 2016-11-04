package no.nsd.qddt.domain.controlconstruct;

import no.nsd.qddt.domain.controlconstructinstruction.ControlConstructInstructionService;
import no.nsd.qddt.domain.questionItem.QuestionItemService;
import no.nsd.qddt.domain.questionItem.audit.QuestionItemAuditService;
import no.nsd.qddt.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Dag Østgulen Heradstveit
 * @author Stig Norland
 */
@Service("instrumentQuestionService")
class ControlConstructServiceImpl implements ControlConstructService {

    private ControlConstructRepository controlConstructRepository;
    private ControlConstructInstructionService cciService;
    private QuestionItemAuditService qAuditService;
    private QuestionItemService  qiService;


    @Autowired
    public ControlConstructServiceImpl(ControlConstructRepository controlConstructRepository,
                                       ControlConstructInstructionService cciService,
                                       QuestionItemAuditService questionAuditService,
                                       QuestionItemService questionItemService) {
        this.controlConstructRepository = controlConstructRepository;
        this.cciService = cciService;
        this.qAuditService = questionAuditService;
        this.qiService = questionItemService;
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

        ControlConstruct instance = controlConstructRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(id, ControlConstruct.class));

        return postGet(instance);

    }

    @Override
    @Transactional()
    public ControlConstruct save(ControlConstruct instance) {
        instance.populateControlConstructs();
        cciService.save(instance.getControlConstructInstructions());
        instance = controlConstructRepository.save(instance);
        // before returning fetch correct version of QI...
        if (instance.getQuestionItemUUID() != null)
            instance.setQuestionItem(qAuditService.findRevision(instance.getQuestionItemUUID(),instance.getRevisionNumber()).getEntity());
        return instance;
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

    @Override
    @Transactional(readOnly = true)
    public List<ControlConstruct> findByInstrumentId(UUID instrumentId) {
        return postGet(controlConstructRepository.findByInstrumentId(instrumentId));
    }

    @Override
    public List<ControlConstruct> findByQuestionItemUUIDs(List<UUID> questionItemIds) {
        return postGet(controlConstructRepository.findByquestionItemUUIDIn(questionItemIds));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ControlConstruct> findTop25ByQuestionItemQuestion(String question) {

        List<UUID> uuidList = qiService.findByNameLikeAndQuestionLike(question,null,new PageRequest(0,25)).getContent()
                            .stream().map(f->f.getId()).collect(Collectors.toList());

        return findByQuestionItemUUIDs(uuidList);
    }


    /*
    post fetch processing, some elements are not supported by the framework (enver mixed with jpa db queries)
    thus we need to populate some elements ourselves.
     */
    private  ControlConstruct postGet(ControlConstruct instance){
        // instructions has to unpacked into pre and post instructions
        instance.populateInstructions();
        // before returning fetch correct version of QI...
        instance.setQuestionItem(qAuditService.findRevision(instance.getQuestionItemUUID(),instance.getRevisionNumber()).getEntity());
        return instance;
    }

    private  List<ControlConstruct> postGet(List<ControlConstruct>instances) {
        return instances.stream().map(p->postGet(p)).collect(Collectors.toList());
    }
}
