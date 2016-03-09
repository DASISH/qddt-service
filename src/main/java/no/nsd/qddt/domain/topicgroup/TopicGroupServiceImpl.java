package no.nsd.qddt.domain.topicgroup;

import no.nsd.qddt.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * @author Stig Norland
 */
@Service("topicGroupService")
class TopicGroupServiceImpl implements TopicGroupService {

    private TopicGroupRepository topicGroupRepository;

    @Autowired
    public TopicGroupServiceImpl(TopicGroupRepository topicGroupRepository) {
        this.topicGroupRepository = topicGroupRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return topicGroupRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean exists(UUID uuid) {
        return topicGroupRepository.exists(uuid);
    }

    @Override
    @Transactional(readOnly = true)
    public TopicGroup findOne(UUID uuid) {
        return topicGroupRepository.findById(uuid).orElseThrow(
                () -> new ResourceNotFoundException(uuid, TopicGroup.class)
        );
    }


    @Override
    @Transactional(readOnly = false)
    public TopicGroup save(TopicGroup instance) {
        return topicGroupRepository.save(instance);
    }

    @Override
    @Transactional(readOnly = false)
    public List<TopicGroup> save(List<TopicGroup> instances) {
        return topicGroupRepository.save(instances);
    }


    @Override
    @Transactional(readOnly = false)
    public void delete(UUID uuid) {
        topicGroupRepository.delete(uuid);
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(List<TopicGroup> instances) {
        topicGroupRepository.delete(instances);
    }


    @Transactional(readOnly = false)
    public void delete(TopicGroup instance) {
        topicGroupRepository.delete(instance.getId());
    }

    @Override
    public List<TopicGroup> findByStudyId(UUID id) {
        return topicGroupRepository.findByStudyId(id);
    }
}