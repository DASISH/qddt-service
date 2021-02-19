package no.nsd.qddt.domain.classes.treenode;

import no.nsd.qddt.domain.classes.exception.ResourceNotFoundException;
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
    public boolean exists(UUID id) {
        return treeNodeRepository.existsById(id);
    }

    @Override
    @Transactional()
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR') and hasPermission(#instance,'AGENCY')")
    public <S extends TreeNode> S save(S instance) {
        return treeNodeRepository.save( instance );
    }

    @Override
    @Transactional(readOnly = true)
    public TreeNode<?> findOne(UUID id) {
        return treeNodeRepository.findById(id).orElseThrow(
            () -> new ResourceNotFoundException(id, TreeNode.class));
    }


    @Override
    @Transactional()
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR')")
    public void delete(UUID id) {
        treeNodeRepository.deleteById(id);
    }


    protected TreeNode prePersistProcessing(TreeNode instance) {
//        instance.addChild(  )
        return instance;
    }


    protected TreeNode postLoadProcessing(TreeNode instance) {
        return instance;
    }

}
