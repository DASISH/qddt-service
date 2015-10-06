package no.nsd.qddt.domain.topicgroup.web;

import no.nsd.qddt.domain.topicgroup.TopicGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author Stig Norland
 */
@RestController
@RequestMapping("/topicgroup")
public class TopicGroupController {

    private TopicGroupService topicGroupService;

    @Autowired
    public TopicGroupController(TopicGroupService topicGroupService) {
        this.topicGroupService = topicGroupService;
    }

}
