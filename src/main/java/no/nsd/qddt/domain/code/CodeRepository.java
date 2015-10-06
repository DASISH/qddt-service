package no.nsd.qddt.domain.code;

import no.nsd.qddt.domain.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 * @author Stig Norland
 */
@Repository
interface CodeRepository extends BaseRepository<Code,UUID> {

    List<Code> findByNameIgnoreCaseContains(String tags);

}