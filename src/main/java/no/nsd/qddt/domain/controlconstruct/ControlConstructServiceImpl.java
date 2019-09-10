package no.nsd.qddt.domain.controlconstruct;

import no.nsd.qddt.domain.controlconstruct.audit.ControlConstructAuditService;
import no.nsd.qddt.domain.controlconstruct.json.ConstructJsonView;
import no.nsd.qddt.domain.controlconstruct.json.ConstructQuestionJson;
import no.nsd.qddt.domain.controlconstruct.pojo.*;
import no.nsd.qddt.domain.instruction.InstructionService;
import no.nsd.qddt.domain.questionitem.QuestionItem;
import no.nsd.qddt.domain.questionitem.audit.QuestionItemAuditService;
import no.nsd.qddt.domain.universe.UniverseService;
import no.nsd.qddt.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static no.nsd.qddt.domain.controlconstruct.json.Converter.mapConstruct;
import static no.nsd.qddt.domain.controlconstruct.json.Converter.mapConstructView;
import static no.nsd.qddt.utils.FilterTool.defaultOrModifiedSort;
import static no.nsd.qddt.utils.StringTool.likeify;

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
    private final UniverseService uService;
    private final QuestionItemAuditService qiAuditService;


    @Autowired
    public ControlConstructServiceImpl(ControlConstructRepository ccRepository,
                                       ControlConstructAuditService controlConstructAuditService,
                                       InstructionService iService,
                                       UniverseService uService,
                                       QuestionItemAuditService questionAuditService
    ) {
        this.controlConstructRepository = ccRepository;
        this.auditService = controlConstructAuditService;
        this.iService = iService;
        this.uService = uService;
        this.qiAuditService = questionAuditService;
    }

    @Override
    public long count() {
        return controlConstructRepository.count();
    }

    @Override
    public boolean exists(UUID uuid) {
        return controlConstructRepository.existsById(uuid);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR','ROLE_CONCEPT','ROLE_VIEW')")
    public ControlConstruct findOne(UUID id) {

        return controlConstructRepository.findById(id)
            .map( this::postLoadProcessing )
            .orElseThrow(() -> new ResourceNotFoundException(id, ControlConstruct.class));

    }

    @Override
    @Transactional()
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR') and hasPermission(#instance,'AGENCY')")
    public  <S extends ControlConstruct> S save(S instance) {
        return postLoadProcessing(
            controlConstructRepository.save(
                prePersistProcessing(instance)));
    }

    @Transactional()
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR') and hasPermission(#instance,'AGENCY')")
    public List<ControlConstruct> save(List<ControlConstruct> instances) {
        return  instances.stream().map(this::save).collect(Collectors.toList());
    }

    @Override
    @Transactional()
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR')")
    public void delete(UUID uuid) {
        controlConstructRepository.deleteById(uuid);
    }

    @Override
    @Transactional()
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR')")
    public void delete(List<ControlConstruct> instances) {
        controlConstructRepository.deleteAll(instances);
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
    public <S extends ConstructJsonView> Page<S> findBySearcAndControlConstructKind(String kind, String superKind, String name, String description, Pageable pageable) {
        pageable = defaultOrModifiedSort(pageable, "name ASC", "updated DESC");
        if (name.isEmpty()  &&  description.isEmpty() && superKind.isEmpty()) {
            name = "%";
        }

        return controlConstructRepository.findByQuery(kind, superKind, likeify(name), likeify(description), "", "",pageable)
            .map(qi -> mapConstructView(qi));
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR','ROLE_CONCEPT','ROLE_VIEW')")
    public <S extends ConstructJsonView> Page<S>  findQCBySearch(String name, String questionName, String questionText, Pageable pageable) {
        pageable = defaultOrModifiedSort(pageable, "name ASC", "updated DESC");
        if (name.isEmpty()  &&  questionName.isEmpty() && questionText.isEmpty()) {
            name = "%";
        }

        return controlConstructRepository.findByQuery("QUESTION_CONSTRUCT","", name,"", questionName, questionText, pageable)
            .map(qi-> mapConstructView(postLoadProcessing(qi)));
    }

    private <S extends ControlConstruct> S  prePersistProcessing(S instance) {
        assert  (instance != null);
        Optional<Integer> rev= auditService.findLastChange(instance.getId()).getRevisionNumber();
        if (!instance.isBasedOn() && !instance.isNewCopy()) {
            return instance;
        }

        switch (instance.getClassKind()) {
            case "QUESTION_CONSTRUCT":
                if (instance instanceof QuestionConstruct) {
                    QuestionConstruct qc = (QuestionConstruct) instance;
                    qc.populateControlConstructInstructions();

                    qc.getControlConstructInstructions().forEach( cqi -> {
                        if (cqi.getInstruction().getId() == null)
                            cqi.setInstruction( iService.save( cqi.getInstruction() ) );
                    } );

                    qc.getUniverse().forEach( universe -> {
                        if (universe.getId() == null) {
                            uService.save( universe );
                        }
                    } );

                    if (qc.getQuestionItem() != null) {
                        QuestionItem question = qc.getQuestionItem();
                        qc.setQuestionName( question.getName() );
                        qc.setQuestionText( question.getQuestion() );
                    }

                    return (S) new FactoryQuestionConstruct().copy( qc, rev );
                }
                break;
            case "SEQUENCE_CONSTRUCT":
                if (instance instanceof Sequence) {
                    return (S) new FactorySequenceConstruct().copy( (Sequence) instance, rev );
                }
                break;
            case "CONDITION_CONSTRUCT":
                if (instance instanceof ConditionConstruct) {
                    return (S) new FactoryConditionConstruct().copy( (ConditionConstruct) instance, rev );
                }
                break;
            case "STATEMENT_CONSTRUCT":
                if (instance instanceof StatementItem) {
                    return (S) new FactoryStatementConstruct().copy( (StatementItem) instance, rev );
                }
        }
        // This statement shouldn't be reached....
        return instance;
    }

    private  <S extends ControlConstruct> S  postLoadProcessing(S instance) {
        assert  (instance != null);
        if ( instance instanceof QuestionConstruct) {
            QuestionConstruct qc = (QuestionConstruct)instance;
            qc.populateInstructions();                // instructions has to be unpacked into pre and post instructions
            try {
                if (qc.getQuestionItemUUID() != null) {
                    Revision<Integer, QuestionItem> rev = qiAuditService.getQuestionItemLastOrRevision(
                        qc.getQuestionItemUUID(),
                        qc.getQuestionItemRevision() );

                    qc.setQuestionItemRevision( rev.getRevisionNumber().get() );
                    qc.setQuestionItem( rev.getEntity() );
                }
            } catch (Exception ex) {
                ((QuestionConstruct) instance).setQuestionItemRevision(  0 );
                LOG.error( "CCS QI revision not found, resetting to latest.", ex );
            }
            qc.setChangeComment( null );
            return (S)qc;
        }
        instance.setChangeComment( null );
        return instance;
    }

}
