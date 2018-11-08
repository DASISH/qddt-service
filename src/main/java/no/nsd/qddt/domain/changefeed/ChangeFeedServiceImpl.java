package no.nsd.qddt.domain.changefeed;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Stig Norland
 */
@Service("changeFeedService")
public class ChangeFeedServiceImpl implements ChangeFeedService {

    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private final ChangeFeedRepository repository;

    @Autowired
    ChangeFeedServiceImpl(ChangeFeedRepository repository){
        this.repository = repository;
    }
    @Override
    public long count() {
        return repository.count();
    }

    @Override
    public boolean exists(Long aLong) {
        return repository.exists( aLong );
    }

    @Override
    public <S extends ChangeFeed> S findOne(Long aLong) {
        return (S) repository.findOne( aLong );
    }

    @Override
    public <S extends ChangeFeed> S save(S instance) {
        return repository.save( instance );
    }

    @Override
    public void delete(Long aLong) throws DataAccessException {
        repository.delete( aLong );
    }

    @Override
    public void delete(List<ChangeFeed> instances) throws DataAccessException {
        repository.delete( instances );
    }

    @Override
    public Page<ChangeFeed> findAllPageable(Pageable pageable) {
        return repository.findAll( pageable );
    }
}
