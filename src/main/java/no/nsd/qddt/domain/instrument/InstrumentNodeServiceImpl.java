package no.nsd.qddt.domain.instrument;

import no.nsd.qddt.domain.instrument.pojo.InstrumentNode;
import no.nsd.qddt.domain.treenode.TreeNode;
import no.nsd.qddt.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * @author Stig Norland
 */
@Service("instrumentNodeService")
public class InstrumentNodeServiceImpl implements InstrumentNodeService {

    private final InstrumentNodeRepository instrumentNodeRepository;

    @Autowired
    public InstrumentNodeServiceImpl(InstrumentNodeRepository instrumentNodeRepository) {
        this.instrumentNodeRepository = instrumentNodeRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return instrumentNodeRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean exists(UUID uuid) {
        return instrumentNodeRepository.exists(uuid);
    }

    @Override
    @Transactional()
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR')")
    public <S extends InstrumentNode> S save(S instance) {
        return instrumentNodeRepository.saveAndFlush( instance );
    }



    @Override
    @Transactional(readOnly = true)
    public InstrumentNode<?> findOne(UUID uuid) {
        return instrumentNodeRepository.findById(uuid).orElseThrow(
            () -> new ResourceNotFoundException(uuid, TreeNode.class));
    }


    @Override
    @Transactional()
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR')")
    public void delete(UUID uuid) {
        instrumentNodeRepository.delete(uuid);
    }


    protected InstrumentNode prePersistProcessing(InstrumentNode instance) {
//        instance.addChild(  )
        instance.checkInNodes();
        return instance;
    }


    protected InstrumentNode postLoadProcessing(InstrumentNode instance) {
        return instance;
    }

}
