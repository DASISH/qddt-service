package no.nsd.qddt.domain.controlconstruct;

import no.nsd.qddt.domain.controlconstruct.audit.ControlConstructAuditService;
import no.nsd.qddt.domain.controlconstruct.json.ConstructJson;
import no.nsd.qddt.domain.instruction.InstructionService;
import no.nsd.qddt.domain.questionItem.QuestionItem;
import no.nsd.qddt.domain.questionItem.audit.QuestionItemAuditService;
import no.nsd.qddt.exception.ResourceNotFoundException;
import no.nsd.qddt.exception.StackTraceFilter;
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
@Service("instrumentQuestionService")
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
    public ControlConstruct findOne(UUID id) {

        ControlConstruct instance = controlConstructRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id, ControlConstruct.class));

        return postLoadProcessing(instance);
    }

    @Override
    @Transactional()
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_SUPER','ROLE_USER')")
    public ControlConstruct save(ControlConstruct instance) {
        return postLoadProcessing(
            controlConstructRepository.save(
                prePersistProcessing(instance)));
    }

    @Override
    @Transactional()
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_SUPER','ROLE_USER')")
    public List<ControlConstruct> save(List<ControlConstruct> instances) {
        return  instances.stream().map(this::save).collect(Collectors.toList());
    }

    @Override
    @Transactional()
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_SUPER')")
    public void delete(UUID uuid) {
        controlConstructRepository.delete(uuid);
    }

    @Override
    @Transactional()
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_SUPER')")
    public void delete(List<ControlConstruct> instances) {
        controlConstructRepository.delete(instances);
    }

    @Override
    public List<ConstructJson> findByQuestionItems(List<UUID> questionItemIds) {
        assert (questionItemIds.size() > 0);
        return controlConstructRepository.findByquestionItemUUID(questionItemIds.get(0))
            .stream().map(c-> mapConstruct(postLoadProcessing(c))).collect(Collectors.toList());
    }

    @Override
    public List<ConstructJson> findTop25ByQuestionItemQuestion(String question) {
        try {
            question = question.replace("*","%");
            Pageable pageable = defaultOrModifiedSort(new PageRequest(0,25),"question ASC","name ASC");
            return controlConstructRepository.findByQuery(
                    ControlConstructKind.QUESTION_CONSTRUCT.toString(),
                    "%","%",question,pageable)
                    .map(qi-> mapConstruct(postLoadProcessing(qi))).getContent();
        } catch (Exception ex) {
            LOG.error("findTop25ByQuestionItemQuestion",ex);
            StackTraceFilter.filter(ex.getStackTrace()).stream()
                    .map(a->a.toString())
                    .forEach(LOG::info);
            return null;
        }
    }


    @Override
    public Page<ConstructJson> findByNameLikeAndControlConstructKind(String name, String question, ControlConstructKind kind, Pageable pageable) {
        name = name.replace("*","%");
        try {
            pageable = defaultOrModifiedSort(pageable, "name ASC", "updated DESC");
//            System.out.println(name + " - " + pageable);
            return controlConstructRepository.findByQuery(
                kind.toString(), name, name, question ,pageable)
                .map(qi -> mapConstruct(postLoadProcessing(qi)));
        } catch (Exception ex) {
            LOG.error("findByNameLikeAndControlConstructKind",ex);
            StackTraceFilter.filter(ex.getStackTrace()).stream()
                    .map(a->a.toString())
                    .forEach(LOG::info);
            return null;
        }
    }

    private ControlConstruct prePersistProcessing(ControlConstruct instance) {
        instance.populateControlConstructInstructions();

        if(instance.isBasedOn()) {
            Long rev= auditService.findLastChange(instance.getId()).getRevisionNumber().longValue();
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

    private ControlConstruct postLoadProcessing(ControlConstruct instance) {
        assert  (instance != null);
        try{
            // instructions has to be unpacked into pre and post instructions
            instance.populateInstructions();

            // before returning fetch correct version of QI...
            if (instance.getQuestionItemUUID() == null)
                instance.setQuestionItemRevision(0L);
            else {
                Revision<Integer, QuestionItem> rev = qiAuditService.getQuestionItemLastOrRevision(
                        instance.getQuestionItemUUID(),
                        instance.getQuestionItemRevision().intValue());
                instance.setQuestionItemRevision(rev.getRevisionNumber().longValue());
                instance.setQuestionItem(rev.getEntity());
            }
            instance.getChildren().forEach(this::postLoadProcessing);
        } catch (Exception ex){
            LOG.error("postLoadProcessing",ex);
            StackTraceFilter.filter(ex.getStackTrace()).stream()
                .map(a->a.toString())
                .forEach(LOG::info);
        }

        return instance;
    }


}
