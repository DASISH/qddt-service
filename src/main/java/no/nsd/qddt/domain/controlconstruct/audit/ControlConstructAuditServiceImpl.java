package no.nsd.qddt.domain.controlconstruct.audit;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.controlconstruct.ControlConstruct;
import no.nsd.qddt.domain.questionItem.QuestionItem;
import no.nsd.qddt.domain.questionItem.audit.QuestionItemAuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static no.nsd.qddt.utils.FilterTool.defaultSort;

/**
 * @author Dag Østgulen Heradstveit
 */
@Service("instrumentAuditQuestionService")
class ControlConstructAuditServiceImpl implements ControlConstructAuditService {

    private ControlConstructAuditRepository controlConstructAuditRepository;
    private QuestionItemAuditService qiAuditService;


    @Autowired
    public ControlConstructAuditServiceImpl(ControlConstructAuditRepository controlConstructAuditRepository,QuestionItemAuditService questionItemAuditService) {
        this.controlConstructAuditRepository = controlConstructAuditRepository;
        this.qiAuditService = questionItemAuditService;
    }

    @Override
    @Transactional(readOnly = true)
    public Revision<Integer, ControlConstruct> findLastChange(UUID id) {
        Revision<Integer, ControlConstruct> rev =  controlConstructAuditRepository.findLastChangeRevision(id);
        return new Revision<>(rev.getMetadata(),setInstructionAndRevisionedQI(rev.getEntity()));
    }

    @Override
    @Transactional(readOnly = true)
    public Revision<Integer, ControlConstruct> findRevision(UUID id, Integer revision) {
        Revision<Integer, ControlConstruct> rev =  controlConstructAuditRepository.findRevision(id, revision);
        return new Revision<>(rev.getMetadata(),setInstructionAndRevisionedQI(rev.getEntity()));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Revision<Integer, ControlConstruct>> findRevisions(UUID id, Pageable pageable) {
        return controlConstructAuditRepository.findRevisions(id,pageable)
                .map(c-> new Revision<>(c.getMetadata(), setInstructionAndRevisionedQI(c.getEntity())));
    }

    @Override
    public Revision<Integer, ControlConstruct> findFirstChange(UUID uuid) {
        PageRequest pageable = new PageRequest(0,1);
        Revision<Integer, ControlConstruct> rev =
                controlConstructAuditRepository.findRevisions(uuid,
                        defaultSort(pageable,"RevisionNumber DESC")).getContent().get(0);

//        Revision<Integer, ControlConstruct> rev = controlConstructAuditRepository.findRevisions(uuid).
//                getContent().stream().min((i,o)->i.getRevisionNumber()).get();
        setInstructionAndRevisionedQI(rev.getEntity());
        return  new Revision<>(rev.getMetadata(),setInstructionAndRevisionedQI(rev.getEntity()));
    }

    @Override
    public Page<Revision<Integer, ControlConstruct>> findRevisionsByChangeKindNotIn(UUID id, Collection<AbstractEntityAudit.ChangeKind> changeKinds, Pageable pageable) {
        int skip = pageable.getOffset();
        int limit = pageable.getPageSize();
        return new PageImpl<>(
                controlConstructAuditRepository.findRevisions(id).getContent().stream()
                        .filter(f->!changeKinds.contains(f.getEntity().getChangeKind()))
                        .skip(skip)
                        .limit(limit)
                        .map(c->new Revision<>(c.getMetadata(),setInstructionAndRevisionedQI(c.getEntity())))
                        .collect(Collectors.toList())
        );
    }


    /*
post fetch processing, some elements are not supported by the framework (enver mixed with jpa db queries)
thus we need to populate some elements ourselves.
 */
    private  ControlConstruct setInstructionAndRevisionedQI(ControlConstruct instance){
        assert  (instance != null);
        try{
            // FIX BUG instructions doesn't load within ControlConstructAuditServiceImpl, by forcing read here, it works...
            // https://github.com/DASISH/qddt-client/issues/350
            instance.getControlConstructInstructions().forEach(cci-> System.out.println(cci.getInstruction()));
            //            instance.getControlConstructInstructions().forEach(cci->cci.getInstruction().toString());

            instance.populateInstructions();

            if(instance.getQuestionItemUUID() != null) {
                if (instance.getQuestionItemRevision() == null || instance.getQuestionItemRevision() <= 0) {
                    Revision<Integer, QuestionItem> rev = qiAuditService.findLastChange(instance.getQuestionItemUUID());
                    instance.setQuestionItemRevision(rev.getRevisionNumber());
                    instance.setQuestionItem(rev.getEntity());
                } else {
                    QuestionItem qi  =qiAuditService.findRevision(instance.getQuestionItemUUID(), instance.getQuestionItemRevision()).getEntity();
                    instance.setQuestionItem(qi);
                }
            }
            else
                instance.setQuestionItemRevision(0);
        } catch (Exception ex){
            ex.printStackTrace();
            System.out.println(ex.getMessage());
        }

        return instance;
    }

    private List<ControlConstruct> setInstructionAndRevisionedQI(List<ControlConstruct>instances) {
        return instances.stream().map(p-> setInstructionAndRevisionedQI(p)).collect(Collectors.toList());
    }


}
