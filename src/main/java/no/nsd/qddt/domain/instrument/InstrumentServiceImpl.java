package no.nsd.qddt.domain.instrument;

import no.nsd.qddt.domain.controlconstruct.pojo.ConditionKind;
import no.nsd.qddt.domain.elementref.ElementKind;
import no.nsd.qddt.domain.instrument.audit.InstrumentAuditService;
import no.nsd.qddt.domain.instrument.pojo.*;
import no.nsd.qddt.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.StringReader;
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
       return  instrumentRepository.save( prePersistProcessing( instance) );
    }


    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR')")
    public void delete(UUID uuid) {
        instrumentRepository.delete(uuid);
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
    private InstrumentNode loadDetail(InstrumentNode element) {
        element.getParameters().forEach( p -> ((Parameter)p).setId( element.id ));
        if (element.getElementKind() == ElementKind.CONDITION_CONSTRUCT &&  element.getElement() == null) {
            try {
                JsonReader reader = Json.createReader( new StringReader( element.getName() ) );
                JsonObject jsonObject = reader.readObject();
                ConditionNode condNode = new ConditionNode();

                condNode.setId( UUID.fromString( jsonObject.getString( "id" ) ) );
                condNode.setName( jsonObject.getString( "name" ) );
                condNode.setConditionKind( ConditionKind.valueOf( jsonObject.getString( "conditionKind" ) ));
                condNode.setClassKind( jsonObject.getString( "classKind" ) );
                condNode.setCondition( jsonObject.getString( "condition" ) );

                element.setElement( condNode );
                LOG.info("setElement( condNode )");
            } catch (Exception ex) {
                LOG.error(ex.getMessage(), ex.getCause() );
            }
        }
        element.children.forEach( c->  loadDetail( (InstrumentNode) c ) );
        return element;
    }


}
