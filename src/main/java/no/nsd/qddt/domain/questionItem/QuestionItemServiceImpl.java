package no.nsd.qddt.domain.questionItem;

import no.nsd.qddt.domain.category.Category;
import no.nsd.qddt.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * @author Stig Norland
 */
@Service("questionItemService")
class QuestionItemServiceImpl implements QuestionItemService {


    private QuestionItemRepository questionItemRepository;

    @Autowired
    public QuestionItemServiceImpl(QuestionItemRepository questionItemRepository) {
        this.questionItemRepository = questionItemRepository;
    }

    @Override
    public long count() {
        return questionItemRepository.count();
    }

    @Override
    public boolean exists(UUID uuid) {
        return questionItemRepository.exists(uuid);
    }

    @Override
    public QuestionItem findOne(UUID uuid) {
        return questionItemRepository.findById(uuid).orElseThrow(
                () -> new ResourceNotFoundException(uuid, QuestionItem.class)
        );
    }

    @Override
    @Transactional(readOnly = false)
    public QuestionItem save(QuestionItem instance) {

//        instance.setModified(LocalDateTime.now());
        return questionItemRepository.save(instance);
    }

    @Override
    public List<QuestionItem> save(List<QuestionItem> instances) {
        return questionItemRepository.save(instances);
    }

    @Override
    public void delete(UUID uuid) {
        questionItemRepository.delete(uuid);
    }

    @Override
    public void delete(List<QuestionItem> instances) {
        questionItemRepository.delete(instances);
    }

    @Override
    public Page<QuestionItem> getHierarchy(Pageable pageable) {
        return  questionItemRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<QuestionItem> findAllPageable(Pageable pageable){
        return questionItemRepository.findAll(pageable);
    }

    @Override
    public Page<QuestionItem> findByNameLikeAndQuestionLike(String name, String question, Pageable pageable) {
        return questionItemRepository.findByNameLikeAndQuestionQuestionLike(name,question,pageable);
    }

}