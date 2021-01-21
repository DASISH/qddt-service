package no.nsd.qddt.domain.study;

import no.nsd.qddt.domain.classes.interfaces.BaseArchivedRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
@Repository
interface StudyRepository extends BaseArchivedRepository<Study, UUID> {

}
