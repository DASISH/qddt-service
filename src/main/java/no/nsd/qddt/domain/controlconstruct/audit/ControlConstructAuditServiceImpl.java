package no.nsd.qddt.domain.controlconstruct.audit;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.comment.Comment;
import no.nsd.qddt.domain.comment.CommentService;
import no.nsd.qddt.domain.controlconstruct.ControlConstruct;
import no.nsd.qddt.domain.othermaterial.OtherMaterial;
import no.nsd.qddt.domain.othermaterial.OtherMaterialService;
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

import java.util.*;
import java.util.stream.Collectors;

import static no.nsd.qddt.utils.FilterTool.defaultSort;

/**
 * @author Dag Østgulen Heradstveit
 */
@Service("instrumentAuditQuestionService")
class ControlConstructAuditServiceImpl implements ControlConstructAuditService {

    private final ControlConstructAuditRepository controlConstructAuditRepository;
    private final QuestionItemAuditService qiAuditService;
    private final OtherMaterialService otherMaterialService;
    private final CommentService commentService;


    @Autowired
    public ControlConstructAuditServiceImpl(ControlConstructAuditRepository ccAuditRepository
            ,QuestionItemAuditService qAuditService
            ,CommentService cService
            ,OtherMaterialService oService) {
        this.controlConstructAuditRepository = ccAuditRepository;
        this.qiAuditService = qAuditService;
        this.otherMaterialService = oService;
        this.commentService = cService;
    }

    @Override
    @Transactional(readOnly = true)
    public Revision<Integer, ControlConstruct> findLastChange(UUID id) {
        Revision<Integer, ControlConstruct> rev =  controlConstructAuditRepository.findLastChangeRevision(id);
        return new Revision<>(rev.getMetadata(), postLoadProcessing(rev.getEntity()));
    }

    @Override
    @Transactional(readOnly = true)
    public Revision<Integer, ControlConstruct> findRevision(UUID id, Integer revision) {
        Revision<Integer, ControlConstruct> rev =  controlConstructAuditRepository.findRevision(id, revision);
        return new Revision<>(rev.getMetadata(), postLoadProcessing(rev.getEntity()));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Revision<Integer, ControlConstruct>> findRevisions(UUID id, Pageable pageable) {
        return controlConstructAuditRepository.findRevisions(id,pageable)
                .map(c-> new Revision<>(c.getMetadata(), postLoadProcessing(c.getEntity())));
    }

    @Override
    public Revision<Integer, ControlConstruct> findFirstChange(UUID uuid) {
        PageRequest pageable = new PageRequest(0,1);
        Revision<Integer, ControlConstruct> rev =
                controlConstructAuditRepository.findRevisions(uuid,
                        defaultSort(pageable,"RevisionNumber DESC")).getContent().get(0);

        postLoadProcessing(rev.getEntity());
        return  new Revision<>(rev.getMetadata(), postLoadProcessing(rev.getEntity()));
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
                        .map(c->new Revision<>(c.getMetadata(), postLoadProcessing(c.getEntity())))
                        .collect(Collectors.toList())
        );
    }


    /*
post fetch processing, some elements are not supported by the framework (enver mixed with jpa db queries)
thus we need to populate some elements ourselves.
 */
    private  ControlConstruct postLoadProcessing(ControlConstruct instance){
        assert  (instance != null);
        try{
            // FIX BUG instructions doesn't load within ControlConstructAuditServiceImpl, by forcing read here, it works...
            // https://github.com/DASISH/qddt-client/issues/350
            System.out.println("postLoadProcessing instruction -> " + instance.getControlConstructInstructions().size());
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

            // Manually load none audited elements

            List<OtherMaterial> oms = otherMaterialService.findBy(instance.getId());
            instance.setOtherMaterials(new HashSet<>(oms));

            List<Comment> coms = commentService.findAllByOwnerId(instance.getId());
            instance.setComments(new HashSet<>(coms));

        } catch (Exception ex){
            ex.printStackTrace();
            System.out.println(ex.getMessage());
        }

        return instance;
    }

    private List<ControlConstruct> postLoadProcessing(List<ControlConstruct>instances) {
        return instances.stream().map(this::postLoadProcessing).collect(Collectors.toList());
    }



}
