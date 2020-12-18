package no.nsd.qddt.domain.changefeed;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import static no.nsd.qddt.utils.FilterTool.defaultOrModifiedSort;
import static no.nsd.qddt.utils.StringTool.likeify;

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
        return (S) repository.findById( id ).get();
    }

    @Override
    public <S extends ChangeFeed> S save(S instance) {
        return  instance ;
    }

    @Override
    public void delete(ChangeFeedKey id) throws DataAccessException {
        repository.deleteById( id );
    }

    public void delete(List<ChangeFeed> instances) throws DataAccessException {
        repository.deleteInBatch( instances );
    }

    @Override
    public Page<ChangeFeed> findAllPageable(Pageable pageable) {
        return repository.findAll( defaultOrModifiedSort( pageable, "refModified DESC" ) );
    }

    @Override
    public Page<ChangeFeed> filterbyPageable(String name, String change, String kind, Pageable pageable) {

//        return repository.findByNameLikeIgnoreCaseOrRefChangeKindLikeIgnoreCaseOrRefKindLikeIgnoreCase
        return repository.findByQuery(
            likeify(name),
            likeify(change),
            likeify(kind),
            pageable);
//            defaultOrModifiedSort( pageable, "refModified DESC" ) );
    }
}
