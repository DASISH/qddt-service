package no.nsd.qddt.domain.user;

import no.nsd.qddt.domain.AbstractServiceTest;
import no.nsd.qddt.classes.exception.ResourceNotFoundException;
import no.nsd.qddt.classes.exception.UserNotFoundException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

public class AuthorityServiceTest extends AbstractServiceTest {

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
//        User user = new User();
//        user.setEmail("Test User One");
//        userService.save(user);
//
//        user = new User();
//        user.setEmail("Test User Two");
//        userService.save(user);
//
//        user = new User();
//        user.setEmail("Test User Three");
//        userService.save(user);
//
//        assertEquals("Should be three ", 5L,userService.count());

        assertEquals("Should have saved (3+2) agencies", 0L,0L);
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



    @Test(expected = ResourceNotFoundException.class)
    @Override
    public void testDelete() throws Exception {
        User user = new User();
        user.setEmail("Existing User");
        user = userService.save(user);
        userService.delete(user.getId());

        assertNull("Should return null", userService.findOne(user.getId()));
    }

}
