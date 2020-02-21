package no.nsd.qddt.domain.controlconstruct;

import no.nsd.qddt.domain.controlconstruct.audit.ControlConstructAuditService;
import no.nsd.qddt.domain.controlconstruct.factory.*;
import no.nsd.qddt.domain.controlconstruct.json.ConstructJsonView;
import no.nsd.qddt.domain.controlconstruct.json.ConstructQuestionJson;
import no.nsd.qddt.domain.controlconstruct.json.Converter;
import no.nsd.qddt.domain.controlconstruct.pojo.*;
import no.nsd.qddt.domain.elementref.ElementKind;
import no.nsd.qddt.domain.elementref.ElementLoader;
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
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
    private final ElementLoader<QuestionItem> qidLoader;
    // private final OtherMaterialService oService;


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
        this.qidLoader = new ElementLoader<>( questionAuditService);        // this.oService = oService;
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
        return controlConstructRepository.findByQuestionItemRefElementId(questionItemIds.get(0)).stream()
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
            .map( Converter::mapConstructView );
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

        switch (instance.getClassKind()) {
            case "QUESTION_CONSTRUCT":
                if (instance instanceof QuestionConstruct) {
                    QuestionConstruct qc = (QuestionConstruct)instance;
                    qc.populateControlConstructInstructions();

                    qc.getControlConstructInstructions().forEach(cqi->{
                        if (cqi.getInstruction().getId() == null)
                            cqi.setInstruction(iService.save(cqi.getInstruction()));
                    });

                    qc.getUniverse().forEach(universe->{
                        if(universe.getId() == null) {
                            uService.save( universe );
                        }
                    });

                    if(qc.isBasedOn() || qc.isNewCopy()) {
                        Integer rev= auditService.findLastChange(qc.getId()).getRevisionNumber();
                        qc = new FactoryQuestionConstruct().copy(qc, rev );
                    }
                    instance = (S)qc;
                }
                break;
            case "SEQUENCE_CONSTRUCT":
                if (instance instanceof Sequence) {
                    if(instance.isBasedOn() || instance.isNewCopy()) {

                        Integer rev= auditService.findLastChange(instance.getId()).getRevisionNumber();
                        instance = (S)new FactorySequenceConstruct().copy((Sequence)instance, rev );
                    }
                }
                break;
            case "CONDITION_CONSTRUCT":
                if (instance instanceof ConditionConstruct) {
                    if(instance.isBasedOn() || instance.isNewCopy()) {
                        
                        Integer rev= auditService.findLastChange(instance.getId()).getRevisionNumber();
                        instance =  (S)new FactoryConditionConstruct().copy((ConditionConstruct)instance, rev );
                    }
                }       
                break; 
            case "STATEMENT_CONSTRUCT":
                if (instance instanceof StatementItem) {
                    if(instance.isBasedOn() || instance.isNewCopy()) {
                        
                        Integer rev= auditService.findLastChange(instance.getId()).getRevisionNumber();
                        instance =  (S)new FactoryStatementConstruct().copy((StatementItem)instance, rev );
                    }   
                }
                break;
        }
        return instance;
    }

    private  <S extends ControlConstruct> S  postLoadProcessing(S instance) {
        assert  (instance != null);
        if ( instance instanceof QuestionConstruct) {
            QuestionConstruct qc = (QuestionConstruct)instance;
            qc.populateInstructions();                // instructions has to be unpacked into pre and post instructions
            try {
                if(qc.getQuestionItemRef().getElementId()!= null && qc.getQuestionItemRef().getElement() == null) {
                    qidLoader.fill( qc.getQuestionItemRef() );
                    LOG.info( "QI loaded " + qc.getQuestionItemRef().getElement());
                }
            } catch (Exception ex) {
                LOG.error( "CCS QI revision not found, resetting to latest.", ex );
            }
            qc.setChangeComment( null );
            return (S)qc;
        }
        instance.setChangeComment( null );
        return instance;
    }


    private void loadSequence(Sequence sequence) {
        sequence.getSequence().forEach( seq -> {
            if (seq.getElementKind() == ElementKind.SEQUENCE_CONSTRUCT) {
                seq.setElement( auditService.findRevision(seq.getElementId(),seq.getElementRevision()).getEntity());
            }
        });
    }

}
