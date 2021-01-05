package no.nsd.qddt.domain.controlconstruct;

import no.nsd.qddt.domain.controlconstruct.audit.ControlConstructAuditService;
import no.nsd.qddt.domain.controlconstruct.factory.FactoryConditionConstruct;
import no.nsd.qddt.domain.controlconstruct.factory.FactoryQuestionConstruct;
import no.nsd.qddt.domain.controlconstruct.factory.FactorySequenceConstruct;
import no.nsd.qddt.domain.controlconstruct.factory.FactoryStatementConstruct;
import no.nsd.qddt.domain.controlconstruct.json.ConstructJsonView;
import no.nsd.qddt.domain.controlconstruct.json.ConstructQuestionJson;
import no.nsd.qddt.domain.controlconstruct.json.Converter;
import no.nsd.qddt.domain.controlconstruct.pojo.*;
import no.nsd.qddt.classes.elementref.ElementLoader;
import no.nsd.qddt.domain.instruction.InstructionService;
import no.nsd.qddt.domain.questionitem.QuestionItem;
import no.nsd.qddt.domain.questionitem.audit.QuestionItemAuditService;
import no.nsd.qddt.domain.universe.UniverseService;
import no.nsd.qddt.classes.exception.ResourceNotFoundException;
import no.nsd.qddt.classes.exception.StackTraceFilter;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
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
    private final ElementLoader<QuestionItem> qiLoader;


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
        this.qiLoader = new ElementLoader<>(questionAuditService);
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
    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR')")
    public void delete(UUID uuid) {
        controlConstructRepository.deleteById(uuid);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR','ROLE_CONCEPT','ROLE_VIEW')")
    public List<ConstructQuestionJson> findByQuestionItems(List<UUID> questionItemIds) {
        assert (questionItemIds.size() > 0);
        return controlConstructRepository.findByQuestionItemRefElementId(questionItemIds.get(0)).stream()
            .map(c-> (ConstructQuestionJson)mapConstruct(postLoadProcessing(c)))
            .collect(Collectors.toList());
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR','ROLE_CONCEPT','ROLE_VIEW')")
    public <S extends ConstructJsonView> Page<S> findBySearcAndControlConstructKind(String kind, String superKind, String name, String description, String xmlLang, Pageable pageable) {
        pageable = defaultOrModifiedSort(pageable, "name ASC", "updated DESC");
        if (name.isEmpty()  &&  description.isEmpty() && superKind.isEmpty()) {
            name = "%";
        }

        return controlConstructRepository.findByQuery(kind, superKind, likeify(name), likeify(description), "", "", xmlLang, pageable)
            .map( Converter::mapConstructView );
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR','ROLE_CONCEPT','ROLE_VIEW')")
    public <S extends ConstructJsonView> Page<S>  findQCBySearch(String name, String questionName, String questionText, String xmlLang, Pageable pageable) {
        pageable = defaultOrModifiedSort(pageable, "name ASC", "updated DESC");
        if (name.isEmpty()  &&  questionName.isEmpty() && questionText.isEmpty()) {
            name = "%";
        }

        return controlConstructRepository.findByQuery("QUESTION_CONSTRUCT","", name,"", questionName, questionText, xmlLang, pageable)
            .map(qi-> mapConstructView(postLoadProcessing(qi)));
    }


    private <S extends ControlConstruct> S  prePersistProcessing(S instance) {
        assert  (instance != null);
        if (instance instanceof QuestionConstruct) {
            QuestionConstruct qc = (QuestionConstruct)instance;
            qc.getControlConstructInstructions().stream()
                .filter( cqi -> cqi.getInstruction().getId() == null )
                .forEach( cqi -> cqi.setInstruction( iService.save( cqi.getInstruction() ) ) );

            qc.getUniverse().stream()
                .filter( universe -> universe.getId() == null )
                .forEach( uService::save );

            instance = (S)qc;
        }

        if(instance.isBasedOn() || instance.isNewCopy()) {
            Optional<Integer> rev= auditService.findLastChange(instance.getId()).getRevisionNumber();
            switch (instance.getClassKind()) {
                case "QUESTION_CONSTRUCT":
                    instance = (S) new FactoryQuestionConstruct().copy((QuestionConstruct)instance, rev.get() );
                    break;
                case "SEQUENCE_CONSTRUCT":
                    instance = (S)new FactorySequenceConstruct().copy((Sequence)instance, rev.get() );
                    break;
                case "CONDITION_CONSTRUCT":
                    instance =  (S)new FactoryConditionConstruct().copy((ConditionConstruct)instance, rev.get() );
                    break;
                case "STATEMENT_CONSTRUCT":
                    instance =  (S)new FactoryStatementConstruct().copy((StatementItem)instance, rev.get() );
                    break;
            }
        }
        return instance;
    }


    private  <S extends ControlConstruct> S  postLoadProcessing(S instance) {
        assert  (instance != null);
        if ( instance instanceof QuestionConstruct) {
            return loadQuestionConstruct( (QuestionConstruct) instance );
        } else if ( instance instanceof Sequence) {
            loadSequence( (Sequence) instance );
        }
        return instance;
    }

    private <S extends ControlConstruct> S loadQuestionConstruct(QuestionConstruct instance) {
        Hibernate.initialize( instance.getControlConstructInstructions() );
//        instance.populateInstructions();                // instructions has to be unpacked into pre and post instructions
        try {
            if(instance.getQuestionItemRef() != null &&
                instance.getQuestionItemRef().getElementId()!= null &&
                instance.getQuestionItemRef().getElement() == null) {

                qiLoader.fill( instance.getQuestionItemRef() );

            }
        } catch (Exception ex) {
            LOG.error( "CCS QI revision not found " + ex.getMessage() );
        }
        return (S) instance;
    }


    private void loadSequence(Sequence sequence) {
        if (StackTraceFilter.stackContains( "getPdf", "getXml" )) {
            sequence.getSequence().forEach( seq -> {
                seq.setElement( postLoadProcessing(
                    auditService.findRevision( seq.getElementId(), seq.getElementRevision() ).getEntity() ));
            LOG.info( "PDF-XML -> fetched " + seq.getElement().getName() );
            } );
        }
    }
}
