package no.nsd.qddt.domain.user;

import no.nsd.qddt.domain.AbstractServiceTest;
import no.nsd.qddt.exception.ResourceNotFoundException;
import no.nsd.qddt.exception.UserNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class UserServiceTest extends AbstractServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Before
    public void setup() {
        super.setBaseRepositories(userRepository);
    }

    @Test
    public void testFindUserByEmail() throws Exception {
        User user = userService.findByEmail("user@example.org");
        assert user.getUsername().equals("user");
    }

    @Test(expected = UserNotFoundException.class)
    public void testFailFindUserByEmail() throws Exception {
        userService.findByEmail("null");
    }

    @Test
    @Override
    public void testCount() throws Exception {
        User User = new User();
        User.setEmail("Test User One");
        userService.save(User);

        User = new User();
        User.setEmail("Test User Two");
        userService.save(User);

        User = new User();
        User.setEmail("Test User Three");
        userService.save(User);

        assertThat("Should be three", userService.count(), is(3L));
    }

    @Test
    @Override
    public void testExists() throws Exception {
        User User = new User();
        User.setEmail("Existing User");
        User = userService.save(User);
        assertTrue("User should exist", userService.exists(User.getId()));
    }

    @Test
    @Override
    public void testFindOne() throws Exception {
        User User = new User();
        User.setEmail("Existing User");
        User = userService.save(User);
        assertNotNull("User should not be null", userService.findOne(User.getId()));
    }

    @Test
    @Override
    public void testSave() throws Exception {
        User User = new User();
        User.setEmail("Existing User");
        assertNotNull("User should be saved", userService.save(User));
    }

    @Test
    @Override
    public void testSaveAll() throws Exception {
        List<User> agencyList = new ArrayList<>();
        User User = new User();
        User.setEmail("Test User One");
        agencyList.add(User);

        User = new User();
        User.setEmail("Test User Two");
        agencyList.add(User);

        User = new User();
        User.setEmail("Test User Three");
        agencyList.add(User);

        userService.save(agencyList);

        assertEquals("Should have saved 3 agencies", userService.count(), 3L);
    }

    @Test(expected = ResourceNotFoundException.class)
    @Override
    public void testDelete() throws Exception {
        User User = new User();
        User.setEmail("Existing User");
        User = userService.save(User);
        userService.delete(User.getId());

        assertNull("Should return null", userService.findOne(User.getId()));
    }

    @Test(expected = ResourceNotFoundException.class)
    @Override
    public void testDeleteAll() throws Exception {
        List<User> agencyList = new ArrayList<>();
        User User = new User();
        User.setEmail("Test User One");
        agencyList.add(User);

        User = new User();
        User.setEmail("Test User Two");
        agencyList.add(User);

        User = new User();
        User.setEmail("Test User Three");
        agencyList.add(User);

        agencyList = userService.save(agencyList);
        userService.delete(agencyList);

        agencyList.forEach(a -> assertNull("Should return null", userService.findOne(a.getId())));

    }
}