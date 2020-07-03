package no.nsd.qddt.domain.instrument;

import no.nsd.qddt.domain.controlconstruct.pojo.ControlConstruct;
import no.nsd.qddt.domain.controlconstruct.pojo.Sequence;
import no.nsd.qddt.domain.elementref.ElementLoader;
import no.nsd.qddt.domain.elementref.ElementServiceLoader;
import no.nsd.qddt.domain.instrument.audit.InstrumentAuditService;
import no.nsd.qddt.exception.ResourceNotFoundException;
import no.nsd.qddt.exception.StackTraceFilter;
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

import static no.nsd.qddt.utils.FilterTool.defaultOrModifiedSort;
import static no.nsd.qddt.utils.StringTool.likeify;

/**
 * @author Dag Østgulen Heradstveit
 * @author Stig Norland
 */
@Service("instrumentService")
class InstrumentServiceImpl implements InstrumentService {

    private final InstrumentRepository instrumentRepository;
    private final InstrumentAuditService auditService;

    @Autowired
    ElementServiceLoader serviceLoader;

    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public InstrumentServiceImpl(InstrumentRepository instrumentRepository
                                ,InstrumentAuditService instrumentAuditService) {

        this.instrumentRepository = instrumentRepository;
        this.auditService = instrumentAuditService;
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
//        System.out.println("save instrument ");

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

        pageable = defaultOrModifiedSort(pageable, "name ASC", "modified DESC");
        return instrumentRepository.findAll(pageable).map( this::loadListDetail);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InstrumentViewJson> findByNameAndDescriptionPageable(String name, String description,String strKind, String xmlLang, Pageable pageable) {

        pageable = defaultOrModifiedSort(pageable, "name ASC", "modified DESC");
        if (name.isEmpty()  &&  description.isEmpty()) {
            name = "%";
        }
        List<String> kinds = Arrays.stream( InstrumentKind.values() )
            .filter( f -> f.getName().toLowerCase().contains( strKind.toLowerCase() ) && strKind.length() > 1)
            .map( i -> i.toString() )
            .collect( Collectors.toList() );

        LOG.info( kinds.stream().collect( Collectors.joining(", ")) );
        LOG.info( pageable.toString() );
        if (kinds.size() > 0)
            return instrumentRepository.findByQueryAndKinds(likeify(name),likeify(description), kinds, likeify( xmlLang ), pageable)
                .map( this::loadListDetail);

        return instrumentRepository.findByQuery(likeify(name),likeify(description), likeify( xmlLang ), pageable)
            .map( this::loadListDetail);
    }


    protected Instrument prePersistProcessing(Instrument instance) {
        LOG.info("prePersistProcessing");

        Integer rev = null;
        if(instance.isBasedOn())
            rev= auditService.findLastChange(instance.getId()).getRevisionNumber();

        if (instance.isBasedOn() || instance.isNewCopy())
            instance = new InstrumentFactory().copy(instance, rev );

        this.refreshSequence(instance.getSequence());

        return instance;
    }

    private void refreshSequence( List<InstrumentElement> elements ){
        elements.stream().filter( f -> f.getElement() != null)
            .forEach( el -> {
                if (Sequence.class.isInstance( el )) {
                    Sequence seq = Sequence.class.cast( el.getElement() );
                    el.setSequence( seq.getSequence().stream().map( s -> new InstrumentElement(s))
                    .collect( Collectors.toList()) );
                }
            });
    }

    private InstrumentViewJson loadListDetail(Instrument instrument) {
        return new InstrumentViewJson( instrument );
    }


    private Instrument loadDetail(Instrument instrument) {
        instrument.setChangeComment( null );
        Hibernate.initialize(instrument.getSequence());
        if (StackTraceFilter.stackContains( "getPdf", "getXml" )) {
            instrument.getSequence().forEach( this::loadDetail );
        }

        return  instrument;
    }

    private InstrumentElement loadDetail(InstrumentElement element) {
            getDetail( element);
        return element;
    }


    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR','ROLE_CONCEPT','ROLE_VIEW','ROLE_GUEST')")
    public InstrumentElement getDetail(InstrumentElement element) {
        return (InstrumentElement)
            new ElementLoader<ControlConstruct>(
                serviceLoader.getService( element.getElementKind() ) ).fill( element );
    }

}
