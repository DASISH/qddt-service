package service;

import no.nsd.qddt.QDDT;
import no.nsd.qddt.domain.User;
import no.nsd.qddt.exception.UserNotFoundException;
import no.nsd.qddt.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = QDDT.class)
public class UserServiceTest {

    /**
     * User test data
     * admin:password:admin@example.org
     * user:password:user@example.org
     */

    @Autowired
    private UserService userService;

    @Test
    public void testFindUserByEmail() throws Exception {
        User user = userService.findByEmail("user@example.org");
        assert user.getUsername().equals("user");
    }

    @Test(expected = UserNotFoundException.class)
    public void testFailFindUserByEmail() throws Exception {
        userService.findByEmail("null");
    }

}
