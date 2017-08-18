package no.nsd.qddt.domain.concept;

import no.nsd.qddt.domain.concept.audit.ConceptAuditService;
import no.nsd.qddt.domain.conceptquestionitem.ConceptQuestionItem;
import no.nsd.qddt.domain.questionItem.QuestionItem;
import no.nsd.qddt.domain.questionItem.audit.QuestionItemAuditService;
import no.nsd.qddt.domain.topicgroup.TopicGroup;
import no.nsd.qddt.domain.topicgroup.TopicGroupService;
import no.nsd.qddt.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static no.nsd.qddt.utils.FilterTool.defaultSort;

/**
 * @author Stig Norland
 * @author Dag Østgulen Heradstveit
 */

@Service("conceptService")
class ConceptServiceImpl implements ConceptService {

    private final ConceptRepository conceptRepository;
    private final ConceptAuditService auditService;
    private final QuestionItemAuditService questionAuditService;
    private final TopicGroupService topicGroupService;

    @Autowired
      ConceptServiceImpl(ConceptRepository conceptRepository
            , ConceptAuditService conceptAuditService
            , TopicGroupService topicGroupService
            , QuestionItemAuditService questionAuditService){
        this.conceptRepository = conceptRepository;
        this.auditService = conceptAuditService;
        this.topicGroupService = topicGroupService;
        this.questionAuditService = questionAuditService;
    }

    @Override
    public long count() {
        return conceptRepository.count();
    }

    @Override
    public boolean exists(UUID uuid) {
        return conceptRepository.exists(uuid);
    }

    @Override
    public Concept findOne(UUID uuid) {
        return conceptRepository.findById(uuid).map(this::postLoadProcessing).orElseThrow(
//        return conceptRepository.findById(uuid).orElseThrow(
                () -> new ResourceNotFoundException(uuid, Concept.class));

    }

    @Override
    @Transactional()
    public Concept save(Concept instance) {
        return postLoadProcessing(
                conceptRepository.save(
                        prePersistProcessing(instance)));
    }

    @Override
    @Transactional()
    public List<Concept> save(List<Concept> instances) {
        instances.forEach(this::save);
        return instances;
    }

    @Override
    public void delete(UUID uuid) {
        conceptRepository.delete(uuid);
    }

    @Override
    public void delete(List<Concept> instances) {
        conceptRepository.delete(instances);
    }


    private Concept prePersistProcessing(Concept instance) {
        try {
            instance.getConceptQuestionItems().stream()
                    .filter(f->f.getQuestionItemRevision() == null)
                    .forEach(cqi->{
                        Revision<Integer, QuestionItem> rev = questionAuditService.findLastChange(cqi.getId().getQuestionItemId());
                        cqi.setQuestionItemRevision(rev.getRevisionNumber());
                        System.out.println("QuestionItemRevision set to latest revision " + cqi.getQuestionItemRevision());
            });

            if (instance.getId() == null & instance.getTopicRef().getId() != null) {        // load if new/basedon copy
                TopicGroup tg = topicGroupService.findOne(instance.getTopicRef().getId());
                instance.setTopicGroup(tg);
            }

            if (instance.isBasedOn()) {
                Revision<Integer, Concept> lastChange
                        = auditService.findLastChange(instance.getId());
                instance.makeNewCopy(lastChange.getRevisionNumber());
            } else if (instance.isNewCopy()) {
                instance.makeNewCopy(null);
            }
        } catch(Exception ex) {
            System.out.println("ConceptService-> prePersistProcessing " + instance.getName());
            ex.printStackTrace();
        }
        return instance;
    }

    /*
        post fetch processing, some elements are not supported by the framework (enver mixed with jpa db queries)
        thus we need to populate some elements ourselves.
     */
    private Concept postLoadProcessing(Concept instance) {
        assert  (instance != null);
        try{
            for (ConceptQuestionItem cqi :instance.getConceptQuestionItems()) {

                Revision<Integer, QuestionItem> rev = questionAuditService.getQuestionItemLastOrRevision(
                        cqi.getId().getQuestionItemId(),
                        cqi.getQuestionItemRevision());
                cqi.setQuestionItem(rev.getEntity());
                if (!cqi.getQuestionItemRevision().equals(rev.getRevisionNumber())) {
                    System.out.println("ConceptService-> postLoadProcessing: MISSMATCH; wanted" +cqi.getQuestionItemRevision() + " got->"  +rev.getRevisionNumber() );
                    cqi.setQuestionItemRevision(rev.getRevisionNumber());
                }
            }
        } catch (Exception ex){
            System.out.println("postLoadProcessing... " + instance.getName());
            ex.printStackTrace();
            System.out.println(ex.getMessage());
        }
        instance.getChildren().forEach(this::postLoadProcessing);
        return instance;
    }


    @Override
    public Page<Concept> findAllPageable(Pageable pageable) {
        Page<Concept> pages = conceptRepository.findAll(defaultSort(pageable,"name","name ASC"));
        pages.map(this::postLoadProcessing);
        return pages;
    }

    @Override
    public Page<Concept> findByTopicGroupPageable(UUID id, Pageable pageable) {
        Page<Concept> pages = conceptRepository.findByTopicGroupIdAndNameIsNotNull(id,
                defaultSort(pageable,"name","name ASC"));
        pages.map(this::postLoadProcessing);
        return pages;
    }

    @Override
    public Page<Concept> findByNameAndDescriptionPageable(String name, String description, Pageable pageable) {
        Page<Concept> pages = conceptRepository.findByNameLikeIgnoreCaseOrDescriptionLikeIgnoreCase(name,description,
                defaultSort(pageable,"name","name ASC"));
        pages.map(this::postLoadProcessing);
        return pages;
    }

    @Override
    public List<Concept> findByQuestionItem(UUID id) {
        return conceptRepository.findByConceptQuestionItemsIdQuestionItemId(id);
    }


}
