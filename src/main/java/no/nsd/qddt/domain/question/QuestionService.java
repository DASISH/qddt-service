package no.nsd.qddt.domain.question;

import no.nsd.qddt.domain.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * @author Stig Norland
 * @author Dag Østgulen Heradstveit
 */
public interface QuestionService extends BaseService<Question, UUID> {

    /**
     *
     * @param pageable
     * @return
     */
    Page<Question> getHierarchy(Pageable pageable);

    Page<Question> findAllPageable(Pageable pageable);

}
