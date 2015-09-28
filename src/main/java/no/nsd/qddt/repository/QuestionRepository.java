package no.nsd.qddt.repository;

import no.nsd.qddt.domain.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.envers.repository.support.EnversRevisionRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author Stig Norland
 */
@Repository
public interface QuestionRepository extends BaseRepository<Question,UUID>, EnversRevisionRepository<Question, UUID, Integer> {

    Page<Question> findAllByParentId(UUID parentId, Pageable pageable);

//    Page<Question> findQuestionConcept(Long id, Pageable pageable);

//    Page<Question> findQuestionInstrument(Long id, Pageable pageable);
}

