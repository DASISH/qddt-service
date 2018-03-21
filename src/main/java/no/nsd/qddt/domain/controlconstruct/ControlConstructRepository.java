package no.nsd.qddt.domain.controlconstruct;

import no.nsd.qddt.domain.BaseRepository;
import no.nsd.qddt.domain.controlconstruct.pojo.ControlConstruct;
import no.nsd.qddt.domain.controlconstruct.pojo.QuestionConstruct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
    List<QuestionConstruct> findByquestionItemUUID(UUID questionId);


    @Query(value = "SELECT cc.* FROM CONTROL_CONSTRUCT cc " +
            "LEFT JOIN QUESTION_ITEM qi ON qi.id = cc.questionItem_id " +
            "WHERE cc.control_construct_kind = :kind AND " +
            "( cc.name ILIKE '%'||:name||'%' or qi.name ILIKE  '%'||:questionName||'%' or qi.question ILIKE  '%'||:questionText||'%' ) "
           + "ORDER BY ?#{#pageable}"
            ,countQuery = "SELECT count(cc.*)  FROM CONTROL_CONSTRUCT cc " +
            "LEFT JOIN QUESTION_ITEM qi ON qi.id = cc.questionItem_id " +
            "WHERE cc.control_construct_kind = :kind AND " +
            "( cc.name ILIKE '%'||:name||'%' or qi.name ILIKE  '%'||:questionName||'%' or qi.question ILIKE  '%'||:questionText||'%' ) "
            ,nativeQuery = true)
    Page<QuestionConstruct> findByQuery(@Param("kind")String kind,
                                       @Param("name")String name,
                                       @Param("questionName")String questionName,
                                       @Param("questionText")String questionText,
                                       Pageable pageable);

}
