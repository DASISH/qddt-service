package no.nsd.qddt.domain.classes.treenode;

import no.nsd.qddt.domain.classes.interfaces.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author Stig Norland
 */
@Repository
interface TreeNodeRepository extends BaseRepository<TreeNode<?>, UUID> {

}

