package no.nsd.qddt.domain.concept;

import no.nsd.qddt.domain.concept.audit.ConceptAuditService;
import no.nsd.qddt.domain.conceptquestionitem.ConceptQuestionItem;
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

    private ConceptRepository conceptRepository;
    private ConceptAuditService auditService;
    private QuestionItemAuditService questionAuditService;
    private TopicGroupService topicGroupService;

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
        return conceptRepository.findById(uuid).map(c-> postLoadProcessing(c)).orElseThrow(
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
        instances.stream().forEach(c-> save(c));
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


    protected Concept prePersistProcessing(Concept instance) {
        instance.harvestQuestionItems();
        instance = syncQuestionItemRev(instance);

        try {
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
            System.out.println("prePersistProcessing");
            ex.printStackTrace();
        }
        return instance;
    }

    /*
        post fetch processing, some elements are not supported by the framework (enver mixed with jpa db queries)
        thus we need to populate some elements ourselves.
     */
    protected Concept postLoadProcessing(Concept instance) {
        assert  (instance != null);
        try{
            // this work as long as instance.getQuestionItems() hasn't been called yet for this instance
            for (ConceptQuestionItem cqi :instance.getConceptQuestionItems()) {
                if (cqi.getQuestionItem().getVersion().getRevision() != null) {
                    System.out.println("Fetched revisioned QI "  + cqi.getQuestionItem().getVersion().getRevision());
                    cqi.setQuestionItem(questionAuditService.findRevision(
                            cqi.getQuestionItem().getId(),
                            cqi.getQuestionItemRevision())
                            .getEntity());
                }
                else {
                    cqi.setQuestionItemRevision(questionAuditService.findLastChange(cqi.getQuestionItem().getId()).getRevisionNumber());
                    System.out.println("QuestionItemRevision set to latest revision " + cqi.getQuestionItem().getVersion().getRevision());
                }
            }
            instance.populateQuestionItems();
        } catch (Exception ex){
            ex.printStackTrace();
            System.out.println(ex.getMessage());
        }
        return instance;
    }


    @Override
    public Page<Concept> findAllPageable(Pageable pageable) {
        Page<Concept> pages = conceptRepository.findAll(defaultSort(pageable,"name","modified DESC"));
        pages.map(c-> postLoadProcessing(c));
        return pages;
    }

    @Override
    public Page<Concept> findByTopicGroupPageable(UUID id, Pageable pageable) {
        Page<Concept> pages = conceptRepository.findByTopicGroupIdAndNameIsNotNull(id,
                defaultSort(pageable,"name","modified DESC"));
        pages.map(c-> postLoadProcessing(c));
        return pages;
    }

    @Override
    public Page<Concept> findByNameAndDescriptionPageable(String name, String description, Pageable pageable) {
        Page<Concept> pages = conceptRepository.findByNameLikeIgnoreCaseOrDescriptionLikeIgnoreCase(name,description,
                defaultSort(pageable,"name","modified DESC"));
        pages.map(c-> postLoadProcessing(c));
        return pages;
    }


    private Concept syncQuestionItemRev(Concept instance){
        assert  (instance != null);
        Concept fromDB= null;
        try{
            if (instance.getId() != null)
                fromDB = conceptRepository.findOne(instance.getId());

            if (fromDB != null)
                fromDB.merge(instance);
            else
                fromDB = instance;

            fromDB.getConceptQuestionItems().stream().filter(f->f.getQuestionItemRevision() == null)
                    .forEach(cqi-> {
                        cqi.setQuestionItemRevision(
                                questionAuditService.findLastChange(
                                        cqi.getId().getQuestionItemId()).getRevisionNumber());
                        System.out.println("Revision set for " + cqi.getQuestionItem().getName() +" - " +cqi.getQuestionItem().getVersion().getRevision());
                    });


        } catch (Exception ex){
            ex.printStackTrace();
            System.out.println(ex.getMessage());
        }
        return fromDB;
    }

}
