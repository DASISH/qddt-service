package no.nsd.qddt.controller;

import no.nsd.qddt.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Stig Norland
 */
@RestController
@RequestMapping("/hashTag")
public class TagsController {

    @Autowired
    private TagService tagService;

}

