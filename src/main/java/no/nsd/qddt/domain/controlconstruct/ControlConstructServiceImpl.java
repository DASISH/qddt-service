package no.nsd.qddt.domain.controlconstruct;

import no.nsd.qddt.domain.questionItem.QuestionItem;
import no.nsd.qddt.domain.questionItem.QuestionItemService;
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

/**
 * @author Dag Østgulen Heradstveit
 * @author Stig Norland
 */
@Service("instrumentQuestionService")
class ControlConstructServiceImpl implements ControlConstructService {

    private ControlConstructRepository controlConstructRepository;
//    private ControlConstructInstructionService cciService;
    private QuestionItemAuditService qiAuditService;
    private QuestionItemService  qiService;


    @Autowired
    public ControlConstructServiceImpl(ControlConstructRepository ccRepository,
//                                       ControlConstructInstructionService cciService,
                                       QuestionItemAuditService questionAuditService,
                                       QuestionItemService questionItemService) {
        this.controlConstructRepository = ccRepository;
//        this.cciService = cciService;
        this.qiAuditService = questionAuditService;
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

        return setInstructionAndRevisionedQI(instance);

    }

    @Override
    @Transactional()
    public ControlConstruct save(ControlConstruct instance) {
        instance.populateControlConstructInstructions();
//        cciService.save(instance.getControlConstructInstructions());
        return setInstructionAndRevisionedQI(
                controlConstructRepository.save(instance));
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
        return setInstructionAndRevisionedQI(controlConstructRepository.findByInstrumentId(instrumentId));
    }

    @Override
    public List<ControlConstruct> findByQuestionItems(List<UUID> questionItemIds) {
        assert (questionItemIds.size() > 0);
        questionItemIds.forEach(System.out::println);
        return setInstructionAndRevisionedQI(controlConstructRepository.findByquestionItemUUID(questionItemIds.get(0)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ControlConstruct> findTop25ByQuestionItemQuestion(String question) {

        List<UUID> uuidList = qiService.findByNameLikeAndQuestionLike(question,null,new PageRequest(0,25)).getContent()
                            .stream().map(f->f.getId()).collect(Collectors.toList());

        return findByQuestionItems(uuidList);
    }

    @Override
    public Page<ControlConstruct> findByNameLikeOrQuestionLike(String name, String question, Pageable pageable) {
        name = name.replace("*","%");
        question = question.replace("*","%");

        return controlConstructRepository.findByInstrumentIsNullAndNameLikeIgnoreCaseOrQuestionItemReferenceOnlyQuestionQuestionLikeIgnoreCase(name,question,pageable)
                .map(qi-> setInstructionAndRevisionedQI(qi));
    }

    @Override
    public Page<ControlConstruct> findByNameLikeAndControlConstructKind(String name, ControlConstructKind kind, Pageable pageable) {
        name = name.replace("*","%");
        return controlConstructRepository.findByNameLikeIgnoreCaseAndControlConstructKind(name,kind,pageable)
                .map(qi-> setInstructionAndRevisionedQI(qi));
    }


    /*
    post fetch processing, some elements are not supported by the framework (enver mixed with jpa db queries)
    thus we need to populate some elements ourselves.
     */
    private  ControlConstruct setInstructionAndRevisionedQI(ControlConstruct instance){
        assert  (instance != null);
        try{
            // instructions has to unpacked into pre and post instructions
            instance.populateInstructions();

            // before returning fetch correct version of QI...
            if(instance.getQuestionItemUUID() != null) {
                if (instance.getQuestionItemRevision() == null || instance.getQuestionItemRevision() <= 0) {
                    Revision<Integer, QuestionItem> rev = qiAuditService.findLastChange(instance.getQuestionItemUUID());
                    instance.setQuestionItemRevision(rev.getRevisionNumber());
                    instance.setQuestionItem(rev.getEntity());
                } else {
                    QuestionItem qi  =qiAuditService.findRevision(instance.getQuestionItemUUID(), instance.getQuestionItemRevision()).getEntity();
                    instance.setQuestionItem(qi);
                }
            }
            else
                instance.setQuestionItemRevision(0);
        } catch (Exception ex){
            ex.printStackTrace();
            System.out.println(ex.getMessage());
        }

        return instance;
    }

    private  List<ControlConstruct> setInstructionAndRevisionedQI(List<ControlConstruct>instances) {
        System.out.println("setInstructionAndRevisionedQI " + instances.size());
        return instances.stream().map(p-> setInstructionAndRevisionedQI(p)).collect(Collectors.toList());
    }
}
