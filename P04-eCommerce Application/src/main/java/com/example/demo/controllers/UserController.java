package com.example.demo.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;

@RestController
@RequestMapping( "/api/user" )
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private CartRepository cartRepository;

    @GetMapping( "/id/{id}" )
    public ResponseEntity<User> findById(@PathVariable Long id) {
        log.info("method={} className={} id={} action={} actionResult={} msg={}"
                , "GET"
                , this.getClass().getSimpleName()
                , id
                , "findById"
                , "successful"
                , "Get user by id.");
//        log.info("Get user by id: {} ", id);

        return ResponseEntity.of(userRepository.findById(id));
    }

    @GetMapping( "/{username}" )
    public ResponseEntity<User> findByUserName(@PathVariable String username) {
        User user = userRepository.findByUsername(username);
        log.info("method={} className={} username={} action={} actionResult={} msg={}"
                , "GET"
                , this.getClass().getSimpleName()
                , username
                , "findByUserName"
                , "successful"
                , "Get user by username.");
//        log.info("Get user by username:{} ", username);
        return user == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(user);
    }

    @PostMapping( "/create" )
    public ResponseEntity<User> createUser(@RequestBody CreateUserRequest createUserRequest)
            throws Exception {
        // Generate one Exception at this line when repository is empty.
        User u = userRepository.findByUsername(createUserRequest.getUsername());

//        final List<User> all = userRepository.findAll();
//        final Set<String> userNames = new HashSet<>();
//        for (int i = 0; i < all.size(); i++) {
//            userNames.add(all.get(i).getUsername());
//        }
//        if (userNames.isEmpty()) {
        if (createUserRequest.getPassword().length() < 7) {
            log.info("method={} className={} username={} action={} actionResult={} msg={}"
                    , "POST"
                    , this.getClass().getSimpleName()
                    , createUserRequest.getUsername()
                    , "createUser"
                    , "failue"
                    , "Password length must be more than 7!");
            return ResponseEntity.badRequest().build();
        }
        if (!createUserRequest.getPassword()
                .equals(createUserRequest.getComfirmPassword())) {
            log.info("method={} className={} username={} action={} actionResult={} msg={}"
                    , "POST"
                    , this.getClass().getSimpleName()
                    , createUserRequest.getUsername()
                    , "createUser"
                    , "failue"
                    , "Comfirm Password error!");
//                log.info("CreateUserFailue! Comfirm Password error!!  UserName, {}"
//                        , createUserRequest.getUsername());
            return ResponseEntity.badRequest().build();
        }
//            log.info("className={} username={} action={} actionResult={} reason={}"
//                    , this.getClass().getName()
//                    , createUserRequest.getUsername()
//                    , "createUser"
//                    , "success"
//                    , "Create user is successful!");
        return ResponseEntity.ok(
                saveOneUser(createUserRequest.getUsername()
                        , createUserRequest.getPassword())
        );
    }
//        } else {
//            if (userNames.contains(createUserRequest.getUsername())) {
//                log.info("method={} className={} username={} action={} actionResult={} msg={}"
//                        , "POST"
//                        , this.getClass().getSimpleName()
//                        , createUserRequest.getUsername()
//                        , "createUser"
//                        , "failue"
//                        , "Duplicate user name!");
//                log.info("CreateUserFailue! Duplicate user name!!  UserName, {}"
//                        , createUserRequest.getUsername());
//                return ResponseEntity.badRequest().build();
//            }
//        if (createUserRequest.getPassword().length() < 7) {
//            log.info("method={} className={} username={} action={} actionResult={} msg={}"
//                    , "POST"
//                    , this.getClass().getSimpleName()
//                    , createUserRequest.getUsername()
//                    , "createUser"
//                    , "failue"
//                    , "Password length must be more than 7!");
//            return ResponseEntity.badRequest().build();
//        }
//        if (!createUserRequest.getPassword()
//                .equals(createUserRequest.getComfirmPassword())) {
//            log.info("method={} className={} username={} action={} actionResult={} msg={}"
//                    , "POST"
//                    , this.getClass().getSimpleName()
//                    , createUserRequest.getUsername()
//                    , "createUser"
//                    , "failue"
//                    , "Comfirm Password error!");
//            return ResponseEntity.badRequest().build();
//        }
//            log.info("className={} username={} action={} actionResult={} reason={}"
//                    , this.getClass().getName()
//                    , createUserRequest.getUsername()
//                    , "createUser"
//                    , "success"
//                    , "Create user is successful!");
//        return ResponseEntity.ok(
//                saveOneUser(createUserRequest.getUsername()
//                        , createUserRequest.getPassword()));
//    }
//    }

    private User saveOneUser(String username, String password) {
        User user = new User();
        user.setUsername(username);
        Cart cart = new Cart();
        cartRepository.save(cart);
        user.setCart(cart);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        userRepository.save(user);
        log.info("method={} className={} username={} action={} actionResult={} msg={}"
                , "POST"
                , this.getClass().getSimpleName()
                , username
                , "createUser"
                , "successful"
                , "Create user is successful!");
        return user;
    }
}

