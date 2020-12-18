package no.nsd.qddt.classes.treenode;

import no.nsd.qddt.classes.interfaces.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author Stig Norland
 */
@Repository
interface TreeNodeRepository extends BaseRepository<TreeNode<?>, UUID> {

}

