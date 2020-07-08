package no.nsd.qddt.domain.referenced;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author Stig Norland
 */
@Service("referencedService")
public class ReferencedServiceImpl implements ReferencedService {
   protected final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private final ReferencedRepository repository;

    @Autowired
    ReferencedServiceImpl(ReferencedRepository repository){
        this.repository = repository;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public boolean exists(UUID uuid) {
        return false;
    }

    @Override
    public <S extends Referenced> S findOne(UUID uuid) {
        return null;
    }

    @Override
    public <S extends Referenced> S save(S instance) {
        return null;
    }

    @Override
    public void delete(UUID uuid) throws DataAccessException {

    }

    @Override
    public Page findAll( String kind, Pageable pageable) {
        return repository.findAll( pageable );
    }
}
