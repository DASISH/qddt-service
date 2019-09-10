package no.nsd.qddt.domain.search;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * @author Stig Norland
 */
@Service("SearchService")
public class SearchServiceImpl implements SearchService {
    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private final SearchRepository repository;

    @Autowired
    public SearchServiceImpl(SearchRepository repository) {
        this.repository = repository;
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR','ROLE_CONCEPT','ROLE_VIEW')")
    public QddtUrl findPath(UUID id) {
        return repository.findById( id ).get();
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR','ROLE_CONCEPT')")
    public List<QddtUrl> findByName(String name) {
        return repository.findByName( name );
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public List<QddtUrl> findByUserId(UUID userId) {
        return repository.findByUserId( userId );
    }


}
