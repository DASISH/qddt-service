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
    List<QuestionConstruct> findByQuestionItemRefElementId(UUID questionId);


    @Query(name="findByQuery", nativeQuery = true,
        value = "SELECT cc.* FROM CONTROL_CONSTRUCT cc " +
            "LEFT JOIN AUDIT.QUESTION_ITEM_AUD qi ON qi.id = cc.questionItem_id  AND  qi.rev = cc.questionItem_revision " +
            "WHERE cc.control_construct_kind = :kind AND " +
            "( cc.control_construct_super_kind = :superKind or cc.name ILIKE :name or cc.description ILIKE :description " +
            "or qi.name ILIKE :questionName or qi.question ILIKE :questionText ) "
            + "ORDER BY ?#{#pageable}"
        ,countQuery = "SELECT count(cc.*) FROM CONTROL_CONSTRUCT cc " +
            "LEFT JOIN AUDIT.QUESTION_ITEM_AUD qi ON qi.id = cc.questionItem_id  AND  qi.rev = cc.questionItem_revision " +
            "WHERE cc.control_construct_kind = :kind AND " +
            "( cc.control_construct_super_kind = :superKind or cc.name ILIKE :name or cc.description ILIKE :description " +
            "or qi.name ILIKE :questionName or qi.question ILIKE :questionText ) "
        + " ORDER BY ?#{#pageable}"
    )
    <S extends ControlConstruct> Page<S> findByQuery(@Param("kind")String kind, @Param("superKind")String superKind,
                                                     @Param("name")String name, @Param("description")String desc,
                                                     @Param("questionName")String questionName ,@Param("questionText")String questionText,
                                                     Pageable pageable);


    @Query(name ="removeInstruction", nativeQuery = true,
        value = "DELETE FROM control_construct_instruction cci WHERE cci.control_construct_id = :controlConstructId")
//                "UPDATE cci SET =2 FROM control_construct_instruction cci WHERE cci.control_construct_id = :controlConstructId")
    void removeInstruction(@Param("controlConstructId")UUID controlConstructId);

    @Query(name ="removeUniverse", nativeQuery = true,
        value = "DELETE FROM control_construct_universe ccu WHERE ccu.question_construct_id = :controlConstructId")
    void removeUniverse(@Param("controlConstructId")UUID controlConstructId);

//    @Query(name= "findBySearcAndControlConstructKind", nativeQuery = true,
//        value = "SELECT cc.* FROM CONTROL_CONSTRUCT cc WHERE cc.control_construct_kind = :kind AND " +
//            "(cc.control_construct_super_kind = :super or cc.name ILIKE :name or cc.description ILIKE :description ) "
//            + "ORDER BY ?#{#pageable}"
//        ,countQuery = "SELECT count(cc.*)  FROM CONTROL_CONSTRUCT cc " +
//            "WHERE cc.control_construct_kind = :kind AND " +
//            "(cc.control_construct_super_kind = :super or cc.name ILIKE :name or cc.description ILIKE :description ) ")
//    <S extends ControlConstruct> Page<S> findBySearcAndControlConstructKind(@Param("kind")String kind,@Param("super")String superKind,
//                                                     @Param("name")String name,@Param("description")String desc,
//                                                     Pageable pageable);
}
