package no.nsd.qddt.domain.changefeed;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import static no.nsd.qddt.utils.FilterTool.defaultOrModifiedSort;

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
    public boolean exists(ChangeFeedKey ig) {
        return false;
    }

    @Override
    public <S extends ChangeFeed> S findOne(ChangeFeedKey id) {
        return (S) repository.findOne( id );
    }

    @Override
    public <S extends ChangeFeed> S save(S instance) {
        return  instance ;
    }

    @Override
    public void delete(ChangeFeedKey id) throws DataAccessException {
        repository.delete( id );
    }

    @Override
    public void delete(List<ChangeFeed> instances) throws DataAccessException {
        repository.delete( instances );
    }

    @Override
    public Page<ChangeFeed> findAllPageable(Pageable pageable) {
        PageRequest sort = defaultOrModifiedSort( pageable, "refModified DESC" );
        return repository.findAll( pageable );
    }
}
