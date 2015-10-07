package no.nsd.qddt.domain.user;

import no.nsd.qddt.domain.AbstractServiceTest;
import no.nsd.qddt.exception.UserNotFoundException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

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