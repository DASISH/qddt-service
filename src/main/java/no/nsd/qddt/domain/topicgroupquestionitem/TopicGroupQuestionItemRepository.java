package no.nsd.qddt.domain.topicgroupquestionitem;

import no.nsd.qddt.domain.BaseRepository;
import no.nsd.qddt.domain.conceptquestionitem.ParentQuestionItemId;
import org.springframework.stereotype.Repository;

/**
 * @author Stig Norland
 */
@Repository
public interface TopicGroupQuestionItemRepository extends BaseRepository<TopicGroupQuestionItem, ParentQuestionItemId>{

}
