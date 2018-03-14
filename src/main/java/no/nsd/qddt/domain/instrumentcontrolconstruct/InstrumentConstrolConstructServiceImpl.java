package no.nsd.qddt.domain.instrumentcontrolconstruct;

import no.nsd.qddt.domain.controlconstruct.ControlConstruct;
import no.nsd.qddt.domain.controlconstruct.audit.ControlConstructAuditService;
import no.nsd.qddt.domain.elementref.ElementKind;
import no.nsd.qddt.domain.elementref.ElementLoader;
import no.nsd.qddt.domain.elementref.ElementRef;
import no.nsd.qddt.exception.StackTraceFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author Stig Norland
 */
@Service("instrumentConstrolConstructService")
public class InstrumentConstrolConstructServiceImpl implements InstrumentConstrolConstructService {

    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private final InstrumentConstrolConstructRepository iccRepository;
    private final ElementLoader ccaLoader;


    @Autowired
    public InstrumentConstrolConstructServiceImpl(InstrumentConstrolConstructRepository instrumentConstrolConstructRepository,
                                                 ControlConstructAuditService controlConstructAuditService){

        this.ccaLoader = new ElementLoader( controlConstructAuditService );
        this.iccRepository = instrumentConstrolConstructRepository;

    }

    @Override
    public long count() {
        return iccRepository.count();
    }

    @Override
    public boolean exists(UUID uuid) {
        return iccRepository.exists( uuid );
    }

    @Override
    public InstrumentControlConstruct findOne(UUID uuid) {
        return iccRepository.findOne( uuid );
    }

    @Override
    public <S extends InstrumentControlConstruct> S save(S instance) {
        return iccRepository.save( (S) prePersistProcessing(instance) );
    }

    @Override
    public List<InstrumentControlConstruct> save(List<InstrumentControlConstruct> instances) {
        instances.forEach( c-> prePersistProcessing( c ) );
        return iccRepository.save( instances );
    }

    @Override
    public void delete(UUID uuid) throws DataAccessException {
        iccRepository.delete( uuid );
    }

    @Override
    public void delete(List<InstrumentControlConstruct> instances) throws DataAccessException {
        iccRepository.delete( instances );
    }

    private InstrumentControlConstruct prePersistProcessing(InstrumentControlConstruct instance) {

        if (instance.getControlConstruct() == null ) {
            ElementRef retval = ccaLoader.fill( ElementKind.CONDITION_CONSTRUCT,
                instance.getControlConstructId(),
                instance.getControlConstructRevision() );

            List<String> params = fetchParam(retval.getElement().toString()  );
            instance.setParameters( params.stream()
                .map( p-> new InstrumentParameter(p) )
                .collect( Collectors.toList()));
        }
        return instance;
    }

    private InstrumentControlConstruct postLoadProcessing(InstrumentControlConstruct instance) {
        assert  (instance != null);
        try{
            if (instance.getControlConstruct() == null ) {
                instance.setControlConstruct(
                    (ControlConstruct) ccaLoader.fill( ElementKind.CONDITION_CONSTRUCT,
                        instance.getControlConstructId(),
                        instance.getControlConstructRevision())
                    .getElement() );
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

    private List<String> fetchParam(String value) {
        Pattern pattern = Pattern.compile("[(.*?)]");
        Matcher matcher = pattern.matcher(value);
        List<String> retval = new ArrayList<>( matcher.groupCount());
        for (int i = 0; i < matcher.groupCount(); i++) {
            retval.add( matcher.group( i ) );
        }
        return retval;
    }

}
