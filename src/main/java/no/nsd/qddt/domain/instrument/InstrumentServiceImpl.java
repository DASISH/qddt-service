package no.nsd.qddt.domain.instrument;

import no.nsd.qddt.domain.classes.elementref.ElementKind;
import no.nsd.qddt.domain.instrument.audit.InstrumentAuditService;
import no.nsd.qddt.domain.instrument.pojo.*;
import no.nsd.qddt.domain.classes.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.StringReader;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
    private final InstrumentNodeService instrumentNodeService;

    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public InstrumentServiceImpl(InstrumentRepository instrumentRepository
        , InstrumentAuditService instrumentAuditService, InstrumentNodeService  instrumentNodeService) {

        this.instrumentRepository = instrumentRepository;
        this.auditService = instrumentAuditService;
        this.instrumentNodeService = instrumentNodeService;
    }

    @Override
    public long count() {
        return instrumentRepository.count();
    }

    @Override
    public boolean exists(UUID uuid) {
        return instrumentRepository.existsById(uuid);
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
       return  instrumentRepository.save( prePersistProcessing( instance) );
    }


    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR')")
    public void delete(UUID uuid) {
        instrumentRepository.deleteById(uuid);
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
            .map( Enum::toString )
            .collect( Collectors.toList() );

        LOG.info( String.join( ", ", kinds ) );
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
            rev= auditService.findLastChange(instance.getId()).getRevisionNumber().get();

        if (instance.isBasedOn() || instance.isNewCopy())
            instance = new InstrumentFactory().copy(instance, rev );

        instance.getRoot().checkInNodes();

        instance.setRoot( instrumentNodeService.save( instance.getRoot() ));

        return instance;
    }

    private InstrumentViewJson loadListDetail(Instrument instrument) {
        return new InstrumentViewJson( instrument );
    }

    private Instrument loadDetail(Instrument instrument) {
        instrument.setChangeComment( null );
        instrument.getRoot().children.forEach( c->  loadDetail( (InstrumentNode) c ) );
//        Hibernate.initialize(instrument.getSequence());
//        if (StackTraceFilter.stackContains( "getPdf", "getXml" )) {
//            instrument.getSequence().forEach( this::loadDetail );
//        }
        return  instrument;
    }
//

    private final Pattern TAGS = Pattern.compile("\\[(.{1,25}?)\\]");


    private InstrumentNode loadDetail(InstrumentNode element) {
        element.getParameters().forEach( p -> ((Parameter)p).setId( element.id ));
        if (element.getElementKind() == ElementKind.CONDITION_CONSTRUCT &&  element.getElement() == null) {
            try {
                javax.json.JsonReader jr = javax.json.Json.createReader(new StringReader(element.getName()));
                javax.json.JsonObject objCondition = jr.readObject().getJsonObject("condition");
                String condition = objCondition.toString();
                Matcher matcher = TAGS.matcher(condition);

                element.getParameters().clear();
                element.addParameter( new Parameter( objCondition.getString( "name" ).toUpperCase() , "OUT") );
//                element.clearInParameters();

                while(matcher.find())
                {
                    element.addParameter( new Parameter( matcher.group(1).toUpperCase() , "IN") );
                }
            } catch (Exception ex) {
                LOG.error(ex.getMessage(), ex.getCause() );
            }
        }
        element.children.forEach( c->  loadDetail( (InstrumentNode) c ) );
        return element;
    }


}
