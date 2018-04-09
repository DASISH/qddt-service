package no.nsd.qddt.domain.questionItem;

import no.nsd.qddt.domain.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author Stig Norland
 */
@Repository
public interface QuestionItemRepository extends BaseRepository<QuestionItem,UUID> {

    Page<QuestionItem> findAll(Pageable pageable);

    @Query(value = "SELECT qi.* FROM QUESTION_ITEM qi " +
        "WHERE ( qi.name ILIKE :name or qi.question ILIKE :question or qi.responsedomain_name ILIKE  :responseDomain ) "
        + "ORDER BY ?#{#pageable}"
        ,countQuery = "SELECT qi.* FROM QUESTION_ITEM qi " +
        "WHERE ( qi.name ILIKE :name or qi.question ILIKE :question or qi.responsedomain_name ILIKE  :responseDomain ) "
        ,nativeQuery = true)
    Page<QuestionItem> findByQuery(@Param("name")String name,
                                     @Param("question")String question,
                                     @Param("responseDomain")String responseDomain,
                                     Pageable pageable);
}

