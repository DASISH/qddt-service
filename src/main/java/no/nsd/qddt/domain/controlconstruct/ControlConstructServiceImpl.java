package no.nsd.qddt.domain.controlconstruct;

import no.nsd.qddt.domain.controlconstruct.audit.ControlConstructAuditService;
import no.nsd.qddt.domain.controlconstructinstruction.ControlConstructInstructionService;
import no.nsd.qddt.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Dag Østgulen Heradstveit
 * @author Stig Norland
 */
@Service("instrumentQuestionService")
class ControlConstructServiceImpl implements ControlConstructService {

    private ControlConstructRepository controlConstructRepository;
    private ControlConstructInstructionService cciService;
    private ControlConstructAuditService ccAuditService;

    @Autowired
    public ControlConstructServiceImpl(ControlConstructRepository controlConstructRepository,ControlConstructInstructionService cciService, ControlConstructAuditService ccaService) {
        this.controlConstructRepository = controlConstructRepository;
        this.cciService = cciService;
        this.ccAuditService = ccaService;
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
    public ControlConstruct findOne(UUID id) {
        ControlConstruct instance = controlConstructRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(id, ControlConstruct.class));

        return postGet(instance);

    }

    @Override
    @Transactional()
    public ControlConstruct save(ControlConstruct instance) {
        instance.populateControlConstructs();
        cciService.save(instance.getControlConstructInstructions());
        instance = controlConstructRepository.save(instance);
        // before returning fetch correct version of QI...
        instance.setQuestionItem(ccAuditService.findRevision(instance.getQuestionItemUUID(),instance.getRevisionNumber());
        return instance;
    }

    @Override
    @Transactional()
    public List<ControlConstruct> save(List<ControlConstruct> instances) {

        return  instances.stream().map(this::save).collect(Collectors.toList());

//        instances.forEach(i->{
//            i.populateControlConstructs();
//            cciService.save(i.getControlConstructInstructions());
//        });
//
//        instances = controlConstructRepository.save(instances);
//
//        // before returning fetch correct version of QI...
//        instances.forEach(i->{
//            i.setQuestionItem(ccAuditService.findRevision(i.getQuestionItemUUID(),i.getRevisionNumber());
//        });
//        return instances;
    }

    @Override
    @Transactional()
    public void delete(UUID uuid) {
        controlConstructRepository.delete(uuid);
    }

    @Override
    @Transactional()
    public void delete(List<ControlConstruct> instances) {
        controlConstructRepository.delete(instances);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ControlConstruct> findByInstrumentId(UUID instrumentId) {
        return postGet(controlConstructRepository.findByInstrumentId(instrumentId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ControlConstruct> findByQuestionItemId(UUID questionItemId) {
        return postGet(controlConstructRepository.findByQuestionItemId(questionItemId));
    }

    private  ControlConstruct postGet(ControlConstruct instance){
        // instructions has to unpacked into pre and post instructions
        instance.populateInstructions();
        // before returning fetch correct version of QI...
        instance.setQuestionItem(ccAuditService.findRevision(instance.getQuestionItemUUID(),instance.getRevisionNumber());
        return instance;
    }

    private  List<ControlConstruct> postGet(List<ControlConstruct>instances) {
        return instances.stream().map(p->postGet(p)).collect(Collectors.toList());
    }
}
