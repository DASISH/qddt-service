package no.nsd.qddt.domain.treenode;

import no.nsd.qddt.domain.interfaces.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author Stig Norland
 */
@Repository
interface TreeNodeRepository extends BaseRepository<TreeNode<?>, UUID> {

}

