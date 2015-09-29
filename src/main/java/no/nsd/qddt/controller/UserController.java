package no.nsd.qddt.controller;

import no.nsd.qddt.domain.user.User;
import no.nsd.qddt.domain.user.UserService;
import no.nsd.qddt.utils.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Dag Østgulen Heradstveit
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public User getLoggedInUser() {
        //This can be outdated, but for the prurposes here, its okay.
        return userService.findOne(SecurityContext.getUserDetails().getUser().getId());
    }
}