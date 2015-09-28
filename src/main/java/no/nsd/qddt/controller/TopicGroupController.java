package no.nsd.qddt.controller;

import no.nsd.qddt.domain.TopicGroup;
import no.nsd.qddt.service.TopicGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;


/**
 * @author Stig Norland
 */
@RestController
@RequestMapping("/topicgroup")
public class TopicGroupController extends AbstractAuditController<TopicGroup,UUID> {


    @Autowired
    public TopicGroupController(TopicGroupService topicGroupService) {
        super(topicGroupService);
    }

}
