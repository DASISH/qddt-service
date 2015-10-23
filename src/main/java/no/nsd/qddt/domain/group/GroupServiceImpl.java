package no.nsd.qddt.domain.group;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * @author Stig Norland
 */
@Service("groupService")
public class GroupServiceImpl implements GroupService {

    private GroupRepository groupRepository;

    @Autowired
    public GroupServiceImpl(GroupRepository repository) {
        this.groupRepository = repository;
    }


    @Override
    @Transactional(readOnly = true)
    public long count() {
        return groupRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean exists(UUID uuid) {
        return groupRepository.exists(uuid);
    }

    @Override
    @Transactional(readOnly = true)
    public Group findOne(UUID uuid) {
        return groupRepository.findOne(uuid);
    }

    @Override
    @Transactional(readOnly = false)
    public Group save(Group instance) {
        return groupRepository.save(instance);
    }

    @Override
    @Transactional(readOnly = false)
    public List<Group> save(List<Group> instances) {
        return groupRepository.save(instances);
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(UUID uuid) {
        groupRepository.delete(uuid);
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(List<Group> instances) {
        groupRepository.delete(instances);
    }
}
