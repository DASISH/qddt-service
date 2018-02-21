package no.nsd.qddt.domain.topicgroupquestionitem;

import no.nsd.qddt.domain.BaseService;

import java.util.Set;

/**
 * @author Stig Norland
 */
public interface TopicGroupQuestionItemService extends BaseService<TopicGroupQuestionItem, ParentQuestionItemId> {

    Set<TopicGroupQuestionItem> save(Set<TopicGroupQuestionItem> instances);

}
