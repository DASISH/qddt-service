package no.nsd.qddt.domain.questionitem;

import no.nsd.qddt.domain.classes.interfaces.BaseRepository;
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
        "LEFT JOIN audit.responsedomain_aud r on qi.responsedomain_id = r.id and qi.responsedomain_revision = r.rev " +
        "WHERE ( qi.xml_lang ILIKE :xmlLang AND  (qi.name ILIKE :name or qi.question ILIKE :question or r.name ILIKE :responseDomain )) "
        + "ORDER BY ?#{#pageable}"
        ,countQuery = "SELECT count(qi.id) FROM QUESTION_ITEM qi " +
        "LEFT JOIN audit.responsedomain_aud r on qi.responsedomain_id = r.id and qi.responsedomain_revision = r.rev " +
        "WHERE ( qi.xml_lang ILIKE :xmlLang AND  (qi.name ILIKE :name or qi.question ILIKE :question or r.name ILIKE :responseDomain )) "
        + "ORDER BY ?#{#pageable}"
        ,nativeQuery = true)
    Page<QuestionItem> findByNames(@Param("name")String name,
                                   @Param("question")String question,
                                   @Param("responseDomain")String responseDomain,
                                   @Param("xmlLang")String xmlLang,
                                   Pageable pageable);
}

