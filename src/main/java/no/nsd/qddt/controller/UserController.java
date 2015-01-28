package no.nsd.qddt.controller;

import no.nsd.qddt.domain.User;
import no.nsd.qddt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Stig Norland
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @RequestMapping(value = "/{email}", method = RequestMethod.GET)
    public User findByEmail(@PathVariable("email") String email) {
        return userService.findByEmail(email);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public User findByEmail(@PathVariable("id") Long id) {
        return userService.findById(id);
    }

}
