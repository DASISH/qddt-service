package no.nsd.qddt.domain.concept;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.comment.CommentService;
import no.nsd.qddt.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

/**
 * @author Stig Norland
 * @author Dag Østgulen Heradstveit
 */

@Service("conceptService")
class ConceptServiceImpl implements ConceptService {

    private ConceptRepository conceptRepository;
//    private CommentService commentService;

    @Autowired
//    ConceptServiceImpl(ConceptRepository conceptRepository,CommentService commentService){
      ConceptServiceImpl(ConceptRepository conceptRepository){
        this.conceptRepository = conceptRepository;
//        this.commentService = commentService;
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
    @Transactional(readOnly = true)
    public Concept findOne(UUID uuid) {
        return conceptRepository.findById(uuid).orElseThrow(
                () -> new ResourceNotFoundException(uuid, Concept.class));

    }

    @Override
    @Transactional(readOnly = false)
    public Concept save(Concept instance) {
        return conceptRepository.save(instance);
    }



    @Override
    public List<Concept> save(List<Concept> instances) {
        return conceptRepository.save(instances);
    }

    @Override
    public void delete(UUID uuid) {
        conceptRepository.delete(uuid);
//        try {
//            Concept parent = findOne(uuid).getParent();
//            conceptRepository.delete(uuid);
//            save(parent);
//        } catch (Exception ex) {
//        }
    }

    @Override
    public void delete(List<Concept> instances) {
        conceptRepository.delete(instances);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Concept> findAllPageable(Pageable pageable) {
        Page<Concept> pages = conceptRepository.findAll(pageable);
//        populateComments(pages);
        return pages;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Concept> findByTopicGroupPageable(UUID id, Pageable pageable) {
        Page<Concept> pages = conceptRepository.findByTopicGroupIdAndNameIsNotNull(id,pageable);
//        populateComments(pages);
//        pages.forEach(c-> System.out.println("number of comments:"+ c.getComments().size()));
        return pages;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Concept> findByQuestionItem(UUID questionItemId) {
        return conceptRepository.findByQuestionItemsId(questionItemId);
    }

//    private void populateComments(Page<Concept> pages){
//        for (Concept concept : pages.getContent()) {
//            if (concept.getComments().size() > 0)
//                System.out.println("ALLREADY HAVE COMMNENTS");
//            else
//            concept.setComments(new HashSet<>(commentService.findAllByOwnerIdPageable(concept.getId(),new PageRequest(0,25)).getContent()));
//        }
//    }



}
