package no.nsd.qddt.domain.topicgroup;

import no.nsd.qddt.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * @author Stig Norland
 */
@Service("moduleService")
class TopicGroupServiceImpl implements TopicGroupService {

    private TopicGroupRepository topicGroupRepository;

    @Autowired
    public TopicGroupServiceImpl(TopicGroupRepository topicGroupRepository) {
        this.topicGroupRepository = topicGroupRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return 0;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean exists(UUID uuid) {
        return false;
    }

    @Override
    @Transactional(readOnly = true)
    public TopicGroup findOne(UUID uuid) {
        return topicGroupRepository.findById(uuid).orElseThrow(
                () -> new ResourceNotFoundException(uuid, TopicGroup.class)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<TopicGroup> findAll() {
        return topicGroupRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TopicGroup> findAll(Pageable pageable) { return topicGroupRepository.findAll(pageable); }

    @Override
    public List<TopicGroup> findAll(Iterable<UUID> uuids) {
        return topicGroupRepository.findAll(uuids);
    }


    @Override
    @Transactional(readOnly = false)
    public TopicGroup save(TopicGroup instance) {

        instance.setCreated(LocalDateTime.now());
        return topicGroupRepository.save(instance);
    }


    @Override
    @Transactional(readOnly = true)
    public void delete(UUID uuid) {
        topicGroupRepository.delete(uuid);
    }


    @Transactional(readOnly = false)
    public void delete(TopicGroup instance) { topicGroupRepository.delete(instance.getId());  }


    @Override
    @Transactional(readOnly = true)
    public Revision<Integer, TopicGroup> findLastChange(UUID uuid) {
        return topicGroupRepository.findLastChangeRevision(uuid);
    }

    @Override
    @Transactional(readOnly = true)
    public Revision<Integer, TopicGroup> findEntityAtRevision(UUID uuid, Integer revision) {
        return topicGroupRepository.findEntityAtRevision(uuid, revision);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Revision<Integer, TopicGroup>> findAllRevisionsPageable(UUID uuid, Pageable pageable) {
        return topicGroupRepository.findRevisions(uuid,pageable);
    }
}
