package domain.user;

import domain.AbstractServiceTest;
import no.nsd.qddt.QDDT;
import no.nsd.qddt.domain.user.User;
import no.nsd.qddt.domain.user.UserService;
import no.nsd.qddt.exception.UserNotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

public class UserServiceTest extends AbstractServiceTest {

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