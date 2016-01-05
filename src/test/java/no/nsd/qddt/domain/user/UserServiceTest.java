package no.nsd.qddt.domain.user;

import no.nsd.qddt.domain.AbstractServiceTest;
import no.nsd.qddt.exception.ResourceNotFoundException;
import no.nsd.qddt.exception.UserNotFoundException;
import org.junit.After;
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
        super.setup();
        super.setBaseRepositories(userRepository);
    }

    @After
    @Override
    public void tearDown() {
        List<User> list = userRepository.findAll();
        for (User user:list) {
            if(user.getAgency() == null) {
                userRepository.delete(user);
            }
        }
    }

    @Test
    public void testFindUserByEmail() throws Exception {
        User user = new User();
        user.setUsername("tester");
        user.setEmail("tester@example.org");
        user = userService.save(user);
        assertNotNull("Username should not be null", userService.findByEmail("tester@example.org"));
    }

    @Test(expected = UserNotFoundException.class)
    public void testFailFindUserByEmail() throws Exception {
        userService.findByEmail("null");
    }

    @Test
    @Override
    public void testCount() throws Exception {
        User user = new User();
        user.setEmail("Test User One");
        userService.save(user);

        user = new User();
        user.setEmail("Test User Two");
        userService.save(user);

        user = new User();
        user.setEmail("Test User Three");
        userService.save(user);

        assertThat("Should be three ", 5L,is(userService.count()));
    }

    @Test
    @Override
    public void testExists() throws Exception {
        User user = new User();
        user.setEmail("Existing User");
        user = userService.save(user);
        assertTrue("User should exist", userService.exists(user.getId()));
    }

    @Test
    @Override
    public void testFindOne() throws Exception {
        User user = new User();
        user.setEmail("Existing User");
        user = userService.save(user);
        assertNotNull("User should not be null", userService.findOne(user.getId()));
    }

    @Test
    @Override
    public void testSave() throws Exception {
        User user = new User();
        user.setEmail("Existing User");
        assertNotNull("User should be saved", userService.save(user));
    }

    @Test
    @Override
    public void testSaveAll() throws Exception {
        List<User> agencyList = new ArrayList<>();
        User user = new User();
        user.setEmail("Test User One");
        agencyList.add(user);

        user = new User();
        user.setEmail("Test User Two");
        agencyList.add(user);

        user = new User();
        user.setEmail("Test User Three");
        agencyList.add(user);

        userService.save(agencyList);

        assertEquals("Should have saved (3+2) agencies", 5L,userService.count());
    }

    @Test(expected = ResourceNotFoundException.class)
    @Override
    public void testDelete() throws Exception {
        User user = new User();
        user.setEmail("Existing User");
        user = userService.save(user);
        userService.delete(user.getId());

        assertNull("Should return null", userService.findOne(user.getId()));
    }

    @Test(expected = ResourceNotFoundException.class)
    @Override
    public void testDeleteAll() throws Exception {
        List<User> agencyList = new ArrayList<>();
        User user = new User();
        user.setEmail("Test User One");
        agencyList.add(user);

        user = new User();
        user.setEmail("Test User Two");
        agencyList.add(user);

        user = new User();
        user.setEmail("Test User Three");
        agencyList.add(user);

        agencyList = userService.save(agencyList);
        userService.delete(agencyList);

        agencyList.forEach(a -> assertNull("Should return null", userService.findOne(a.getId())));

    }
}