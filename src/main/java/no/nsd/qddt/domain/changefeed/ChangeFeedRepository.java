package no.nsd.qddt.domain.changefeed;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Stig Norland
 */
@Repository
public interface ChangeFeedRepository extends JpaRepository<ChangeFeed,ChangeFeedKey> {
}
