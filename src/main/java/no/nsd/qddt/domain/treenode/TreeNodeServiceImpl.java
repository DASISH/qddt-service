package no.nsd.qddt.domain.treenode;

import no.nsd.qddt.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * @author Stig Norland
 */
@Service("treeNodeService")
public class TreeNodeServiceImpl implements TreeNodeService {

    private final TreeNodeRepository treeNodeRepository;

    @Autowired
    public TreeNodeServiceImpl(TreeNodeRepository treeNodeRepository) {
        this.treeNodeRepository = treeNodeRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return treeNodeRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean exists(UUID uuid) {
        return treeNodeRepository.exists(uuid);
    }

    @Override
    @Transactional()
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR') and hasPermission(#instance,'AGENCY')")
    public <S extends TreeNode> S save(S instance) {
        return treeNodeRepository.save( instance );
    }

    @Override
    @Transactional(readOnly = true)
    public TreeNode<?> findOne(UUID uuid) {
        return treeNodeRepository.findById(uuid).orElseThrow(
            () -> new ResourceNotFoundException(uuid, TreeNode.class));
    }


    @Override
    @Transactional()
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR')")
    public void delete(UUID uuid) {
        treeNodeRepository.delete(uuid);
    }


    protected TreeNode prePersistProcessing(TreeNode instance) {
//        instance.addChild(  )
        return instance;
    }


    protected TreeNode postLoadProcessing(TreeNode instance) {
        return instance;
    }

}
