package no.nsd.qddt.domain.instrument;

import no.nsd.qddt.domain.controlconstruct.audit.ControlConstructAuditService;
import no.nsd.qddt.domain.elementref.ElementKind;
import no.nsd.qddt.domain.elementref.ElementLoader;
import no.nsd.qddt.domain.elementref.ElementRef;
import no.nsd.qddt.domain.instrument.audit.InstrumentAuditService;
import no.nsd.qddt.exception.ResourceNotFoundException;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());


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
    @Transactional(readOnly = true)
    public Instrument findOne(UUID uuid) {
        return  instrumentRepository.findById(uuid)
            .map(this::loadDetail)
            .orElseThrow(() -> new ResourceNotFoundException(uuid, Instrument.class));
    }

    @Override
    @Transactional()
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR') and hasPermission(#instance,'AGENCY')")
    public Instrument save(Instrument instance) {
        System.out.println("save instrument ");

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
    @Transactional(readOnly = true)
    public List<InstrumentViewJson> findByStudy(UUID studyId) {
        return instrumentRepository.findByStudy(studyId).stream()
            .map( this::loadListDetail).collect( Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InstrumentViewJson> findAllPageable(Pageable pageable) {

        pageable = defaultSort(pageable, "name ASC", "modified DESC");
        return instrumentRepository.findAll(pageable).map( this::loadListDetail);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InstrumentViewJson> findByNameAndDescriptionPageable(String name, String description,String strKind, Pageable pageable) {

        pageable = defaultSort(pageable, "name ASC", "modified DESC");
        if (name.isEmpty()  &&  description.isEmpty()) {
            name = "%";
        }
        InstrumentKind kind = Arrays.stream( InstrumentKind.values() )
            .filter( f -> f.getName().toLowerCase().contains( strKind.toLowerCase() ) && strKind.length() > 1)
            .findFirst().orElse( null );

        return instrumentRepository.findByNameLikeIgnoreCaseOrDescriptionLikeIgnoreCaseOrInstrumentKind(likeify(name),likeify(description),kind ,pageable)
            .map( this::loadListDetail);
    }


    protected Instrument prePersistProcessing(Instrument instance) {
        LOG.info("prePersistProcessing");

        Integer rev = null;
        if(instance.isBasedOn())
            rev= auditService.findLastChange(instance.getId()).getRevisionNumber();

        if (instance.isBasedOn() || instance.isNewCopy())
            instance = new InstrumentFactory().copy(instance, rev );

        // Nødvendig???
//        instance.getSequence().stream().forEach( this::loadDetails );
        return instance;
    }


    private InstrumentViewJson loadListDetail(Instrument instrument) {
        return new InstrumentViewJson( instrument );
    }


    private Instrument loadDetail(Instrument instrument) {
        instrument.setChangeComment( null );
        Hibernate.initialize(instrument.getSequence());
        instrument.getSequence().stream().forEach( this::loadDetails );
        return  instrument;
    }

    private InstrumentElement loadDetails(InstrumentElement instance) {
        LOG.info("loadDetails");
        instance.getSequence().stream().forEach( this::loadDetails );
        return  loadDetail( instance) ;
    }

    private InstrumentElement loadDetail(InstrumentElement element) {

        if ( element.getElementRef().getElementKind() == ElementKind.QUESTION_CONSTRUCT) {
            LOG.info("loadDetail QC");
            ElementRef ref = (ElementRef) ccLoader.fill( element.getElementRef());
            element.setElementRef(ref);
        }
        return element;
    }



}
