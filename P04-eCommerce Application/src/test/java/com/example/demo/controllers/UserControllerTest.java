package com.example.demo.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.example.demo.GenerateTestEnity;
import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserControllerTest {

    private  UserController userController;

    private UserRepository userRepo = mock(UserRepository.class);

    private CartRepository cartRepo = mock(CartRepository.class);

    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp(){
        userController = new UserController();
        TestUtils.injectObject(userController, "userRepository", userRepo);
        TestUtils.injectObject(userController, "cartRepository", cartRepo);
        TestUtils.injectObject(userController,"bCryptPasswordEncoder", encoder);
    }

    @Test
    public void create_user_happy_path() throws Exception{
        when(encoder.encode("testPassword")).thenReturn("thisIsHashed");
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("test");
        r.setPassword("testPassword");
        r.setComfirmPassword("testPassword");

        final ResponseEntity<User> response = userController.createUser(r);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User u = response.getBody();
        assertNotNull(u);
        assertEquals(0, u.getId());
        assertEquals("test", u.getUsername());
        assertEquals("thisIsHashed", u.getPassword());

    }

    @Test
    public void get_user_by_name_test() throws Exception{
        User user = GenerateTestEnity.generateOneUser();
        when(userRepo.findByUsername("test")).thenReturn(user);
        String username = "test";
        final ResponseEntity<User> response = userController.findByUserName(username);
        User u = response.getBody();
        assertNotNull(u);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(0,u.getId());
        assertEquals("test", u.getUsername());
        assertEquals("thisIsHashed",u.getPassword());
        assertNotNull(u.getCart());
    }

    @Test
    public void get_user_by_id_test(){
        User user = GenerateTestEnity.generateOneUser();
        Optional<User> result = Optional.of(user);
        when(userRepo.findById(new Long(0))).thenReturn(result);
        final ResponseEntity<User> response = userController.findById(new Long(0));
        User u = response.getBody();
        assertNotNull(u);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("test",u.getUsername());
        assertEquals("thisIsHashed",u.getPassword());
        assertNotNull(u.getCart());
    }
}
