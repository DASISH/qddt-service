package no.nsd.qddt.domain.instrumentelement;

import no.nsd.qddt.domain.controlconstruct.audit.ControlConstructAuditService;
import no.nsd.qddt.domain.controlconstruct.pojo.QuestionConstruct;
import no.nsd.qddt.domain.controlconstruct.pojo.Sequence;
import no.nsd.qddt.domain.elementref.ElementKind;
import no.nsd.qddt.domain.elementref.ElementLoader;
import no.nsd.qddt.domain.elementref.ElementRef;
import no.nsd.qddt.domain.elementref.typed.ElementRefTyped;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * @author Stig Norland
 */
@Service("instrumentElementService")
class InstrumentElementServiceImpl implements InstrumentElementService  {

    private final InstrumentElementRepository instrumentRepository;
    private final ElementLoader ccLoader;

    @Autowired
    public InstrumentElementServiceImpl(
        InstrumentElementRepository instrumentRepository,ControlConstructAuditService controlConstructService) {

        this.instrumentRepository = instrumentRepository;
        this.ccLoader = new ElementLoader( controlConstructService );
    }


    @Override
    @Transactional(readOnly = true)
    public InstrumentElement getDetail(InstrumentElement element) {
        
         element.setElement(ccLoader.fill( element.getElement() ));

         if ( element.getElement().getElementKind() == ElementKind.QUESTION_CONSTRUCT) {
           String question = ((QuestionConstruct) element.getElement().getElement()).getQuestionItem().getQuestion();
           // TODO question.
           if (question.contains("[")) {
               element.getParameters().add(new InstrumentParameter("[oara]", null));
           }
         }
         return element;
    }

    public List<ElementRef> loadSequence(ElementRefTyped<Sequence> sequence ) {
        sequence.getElement().getSequence().stream().forEach( ccLoader::fill );
        return sequence.getElement().getSequence();
    }


    @Override
    public long count() {
        return instrumentRepository.count();
    }

    @Override
    public boolean exists(UUID uuid) {
        return instrumentRepository.exists( uuid );
    }

    @Override
    public InstrumentElement findOne(UUID uuid) {
        return instrumentRepository.findOne( uuid );
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR')")
    public <S extends InstrumentElement> S save(S instance) {
        return instrumentRepository.save( instance );
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR')")
    public List<InstrumentElement> save(List<InstrumentElement> instances) {
        return instrumentRepository.save( instances );
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR')")
    public void delete(UUID uuid) throws DataAccessException {
        instrumentRepository.delete( uuid );
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR')")
    public void delete(List<InstrumentElement> instances) throws DataAccessException {
        instrumentRepository.delete( instances );
    }
}
