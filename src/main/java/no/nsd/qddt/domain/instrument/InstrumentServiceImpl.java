package no.nsd.qddt.domain.instrument;

import no.nsd.qddt.domain.controlconstruct.audit.ControlConstructAuditService;
import no.nsd.qddt.domain.controlconstruct.pojo.Sequence;
import no.nsd.qddt.domain.elementref.ElementKind;
import no.nsd.qddt.domain.elementref.ElementLoader;
import no.nsd.qddt.domain.elementref.ElementRef;
import no.nsd.qddt.domain.elementref.IElementRef;
import no.nsd.qddt.domain.elementref.typed.ElementRefTyped;
import no.nsd.qddt.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 * @author Stig Norland
 */
@Service("instrumentService")
class InstrumentServiceImpl implements InstrumentService {

    private final InstrumentRepository instrumentRepository;
    private final ElementLoader ccLoader;

    @Autowired
    public InstrumentServiceImpl(InstrumentRepository instrumentRepository
                                ,ControlConstructAuditService controlConstructService) {
        this.instrumentRepository = instrumentRepository;
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
        return postLoadProcessing( instrumentRepository.findById(uuid).orElseThrow(
                () -> new ResourceNotFoundException(uuid, Instrument.class)));
    }

    @Override
    @Transactional()
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR')")
    public Instrument save(Instrument instance) {
        return instrumentRepository.save( prePersistProcessing( instance) );
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR')")
    public List<Instrument> save(List<Instrument> instances) {
        return instrumentRepository.save(instances);
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
        return instrumentRepository.findAll(pageable);
    }

    @Override
    public Page<Instrument> findByNameAndDescriptionPageable(String name, String description, Pageable pageable) {
        return instrumentRepository.findByNameLikeIgnoreCaseOrDescriptionLikeIgnoreCase(name,description,pageable);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR','ROLE_CONCEPT','ROLE_VIEW','ROLE_GUEST')")
    public ElementRef getDetail(ElementRef element) {
        return ccLoader.fill( element );
    }

    public List<ElementRef> loadSequence(ElementRefTyped<Sequence> sequence ) {
        sequence.getElementAs().getSequence().stream().forEach( ccLoader::fill );
        return sequence.getElementAs().getSequence();
    }

    protected Instrument prePersistProcessing(Instrument instance) {
        instance.getSequence().stream()
            .filter( s-> s.getElement().getName().isEmpty() )
            .forEach( s-> ccLoader.fill( s.getElement() ) );
        return instance;
    }


    protected Instrument postLoadProcessing(Instrument instance) {
        instance.getSequence().forEach( s -> postLoadProcessing( s.getElement() ) );
        return instance;
    }


    IElementRef postLoadProcessing(IElementRef instance) {

        if (instance.getElementKind() == ElementKind.SEQUENCE_CONSTRUCT)
            return ccLoader.fill( instance );

        return instance;
    }
}
