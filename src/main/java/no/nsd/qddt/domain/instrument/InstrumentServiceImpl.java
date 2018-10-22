package no.nsd.qddt.domain.instrument;

import no.nsd.qddt.domain.controlconstruct.audit.ControlConstructAuditService;
import no.nsd.qddt.domain.elementref.ElementKind;
import no.nsd.qddt.domain.elementref.ElementLoader;
import no.nsd.qddt.domain.elementref.ElementRef;
import no.nsd.qddt.domain.instrument.audit.InstrumentAuditService;
import no.nsd.qddt.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static no.nsd.qddt.utils.FilterTool.defaultSort;
import static no.nsd.qddt.utils.StringTool.likeify;

/**
 * @author Dag Østgulen Heradstveit
 * @author Stig Norland
 */
@Service("instrumentService")
class InstrumentServiceImpl implements InstrumentService {

    private final InstrumentRepository instrumentRepository;
    private final InstrumentAuditService auditService;
    private final ElementLoader ccLoader;

    @Autowired
    public InstrumentServiceImpl(InstrumentRepository instrumentRepository
                                ,InstrumentAuditService instrumentAuditService
                                ,ControlConstructAuditService controlConstructService) {
                                    
        this.instrumentRepository = instrumentRepository;
        this.auditService = instrumentAuditService;
        this.ccLoader = new ElementLoader( controlConstructService );
    }

    @Override
    public long count() {
        return instrumentRepository.count();
    }

    @Override
    public boolean exists(UUID uuid) {
        return instrumentRepository.exists(uuid);
    }

    @Override
    public Instrument findOne(UUID uuid) {
        return  instrumentRepository.findById(uuid).orElseThrow(
                () -> new ResourceNotFoundException(uuid, Instrument.class));
    }

    @Override
    @Transactional()
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR')")
    public Instrument save(Instrument instance) {
        return  instrumentRepository.save( prePersistProcessing( instance) );
    }


    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR')")
    public void delete(UUID uuid) {
        instrumentRepository.delete(uuid);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR')")
    public void delete(List<Instrument> instances) {
        instrumentRepository.delete(instances);
    }

    @Override
    public List<Instrument> findByStudy(UUID studyId) {
        return instrumentRepository.findByStudy(studyId);
    }

    @Override
    public Page<Instrument> findAllPageable(Pageable pageable) {

        pageable = defaultSort(pageable, "name ASC", "modified DESC");
        return instrumentRepository.findAll(pageable);
    }

    @Override
    public Page<Instrument> findByNameAndDescriptionPageable(String name, String description,InstrumentKind kind, Pageable pageable) {

        pageable = defaultSort(pageable, "name ASC", "modified DESC");
        return instrumentRepository.findByNameLikeIgnoreCaseOrDescriptionLikeIgnoreCaseOrInstrumentKind(likeify(name),likeify(description),kind ,pageable);
    }

//    public List<ElementRef> loadSequence(ElementRefTyped<Sequence> sequence ) {
//
//        sequence.getElement().getSequence().stream().forEach( ccLoader::fill );
//        return sequence.getElement().getSequence();
//    }

    protected Instrument prePersistProcessing(Instrument instance) {
        Integer rev = null;
        if(instance.isBasedOn())
            rev= auditService.findLastChange(instance.getId()).getRevisionNumber();

        if (instance.isBasedOn() || instance.isNewCopy())
            instance = new InstrumentFactory().copy(instance, rev );

        instance.getSequence().stream().forEach( s-> postLoadProcessing( s ) );
        return instance;
    }

    private InstrumentElement postLoadProcessing(InstrumentElement instance) {
        instance.getSequences().stream().forEach( s -> postLoadProcessing( s ) );
        return  loadDetail( instance) ;
    }

    @Transactional(readOnly = true)
    InstrumentElement loadDetail(InstrumentElement element) {
        if ( element.getElementRef().getElementKind() == ElementKind.QUESTION_CONSTRUCT) {
            ElementRef ref = ccLoader.fill( element.getElementRef());
            element.setElementRef(ref);
        }
        return element;
    }
}
