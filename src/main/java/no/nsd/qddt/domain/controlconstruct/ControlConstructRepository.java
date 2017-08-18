package no.nsd.qddt.domain.controlconstruct;

import no.nsd.qddt.domain.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * @author Stig Norland
 * @author Dag Østgulen Heradstveit
 */
@Repository
interface ControlConstructRepository extends BaseRepository<ControlConstruct,UUID> {

    /**
     *
     * @param questionId
     * @return
     */
    List<ControlConstruct> findByquestionItemUUID(UUID questionId);


    List<ControlConstruct> findByquestionItemReferenceOnlyIdIn(List<UUID> questionItemIds);

    Page<ControlConstruct> findByNameLikeIgnoreCaseOrQuestionItemReferenceOnlyQuestionQuestionLikeIgnoreCase(String name, String question, Pageable pageable);

    Page<ControlConstruct> findByNameLikeIgnoreCaseAndControlConstructKind(String name, ControlConstructKind kind, Pageable pageable);

    @Query(value = "SELECT cc.* , cc.updated as modified FROM CONTROL_CONSTRUCT cc " +
            "left join QUESTION_ITEM qi on qi.id = cc.questionItem_id " +
            "left join QUESTION q on q.id = qi.question_id " +
            "WHERE cc.control_construct_kind = ?1 and ( cc.name ILIKE ?2 or qi.name ILIKE ?3 or q.question ILIKE ?4 ) " +
            "ORDER BY ?#{#pageable}",
            countQuery = "SELECT count(cc.*)  FROM CONTROL_CONSTRUCT cc " +
            "left join QUESTION_ITEM qi on qi.id = cc.questionItem_id " +
            "left join QUESTION q on q.id = qi.question_id " +
            "WHERE cc.control_construct_kind = ?1 and ( cc.name ILIKE ?2 or qi.name ILIKE ?3 or q.question ILIKE ?4 )",
            nativeQuery = true)
    Page<ControlConstruct> findByQuery(String kind, String name, String questionName, String questionText, Pageable pageable);
    //findByControlConstructKind
    // AndNameLike
    // OrQuestionItemReferenceOnlyNameLike
    // OrQuestionItemReferenceOnlyQuestionQuestionLike
    // AllIgnoreCase
}
