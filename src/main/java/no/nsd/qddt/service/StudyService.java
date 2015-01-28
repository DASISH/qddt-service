package no.nsd.qddt.service;

import no.nsd.qddt.domain.Study;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;

import java.util.List;

/**
 * @author Dag Østgulen Heradstveit
 * @author Stig Norland
 */
public interface StudyService extends AbstractServiceAudit<Study> {

}
