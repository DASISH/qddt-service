package no.nsd.qddt.domain.changefeed;

import no.nsd.qddt.domain.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author Stig Norland
 */
public interface ChangeFeedService  extends BaseService<ChangeFeed, Long> {
    Page<ChangeFeed> findAllPageable(Pageable pageable);
}
