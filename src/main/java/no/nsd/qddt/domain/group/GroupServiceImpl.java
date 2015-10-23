package no.nsd.qddt.domain.group;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

/**
 * @author Stig Norland
 */
public class GroupServiceImpl implements GroupService {

    private GroupRepository groupRepository;

    @Autowired
    public GroupServiceImpl(GroupRepository repository) {
        this.groupRepository = repository;
    }


    @Override
    public long count() {
        return groupRepository.count();
    }

    @Override
    public boolean exists(UUID uuid) {
        return groupRepository.exists(uuid);
    }

    @Override
    public Group findOne(UUID uuid) {
        return groupRepository.findOne(uuid);
    }

    @Override
    public Group save(Group instance) {
        return groupRepository.save(instance);
    }

    @Override
    public List<Group> save(List<Group> instances) {
        return groupRepository.save(instances);
    }

    @Override
    public void delete(UUID uuid) {
        groupRepository.delete(uuid);
    }

    @Override
    public void delete(List<Group> instances) {
        groupRepository.delete(instances);
    }
}
