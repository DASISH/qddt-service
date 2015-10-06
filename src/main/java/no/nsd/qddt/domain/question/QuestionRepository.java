package no.nsd.qddt.domain.question;

import no.nsd.qddt.domain.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author Stig Norland
 */
@Repository
interface QuestionRepository extends BaseRepository<Question,UUID> {

    Page<Question> findAllByParentId(UUID parentId, Pageable pageable);

//    Page<Question> findQuestionConcept(Long id, Pageable pageable);

//    Page<Question> findQuestionInstrument(Long id, Pageable pageable);
}

