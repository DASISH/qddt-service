package no.nsd.qddt.service;

import no.nsd.qddt.domain.Survey;
import org.springframework.data.history.Revision;

/**
 * @author Dag Østgulen Heradstveit
 * @author Stig Norland
 */
public interface SurveyService extends AbstractServiceAudit<Survey> {

    public Revision<Integer, Survey> findEntityAtRevision(Long id, Integer revision);
}
