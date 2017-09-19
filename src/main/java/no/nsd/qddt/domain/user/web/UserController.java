package no.nsd.qddt.domain.user.web;

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

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public User getLoggedInUser() {
        return userService.findOne(SecurityContext.getUserDetails().getUser().getId());
    }

//    @ResponseStatus(value = HttpStatus.OK)
//    @RequestMapping(value = "/list/by-user", method = RequestMethod.GET)
//    public List<User> listByUser() {
//        User user = SecurityContext.getUserDetails().getUser();
//        user.getAuthorities().
//        return userService.findByAgency(user);
//    }

}