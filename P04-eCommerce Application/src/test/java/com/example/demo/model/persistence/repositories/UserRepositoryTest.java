package com.example.demo.model.persistence.repositories;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import javax.persistence.EntityManager;
import javax.sql.DataSource;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@RunWith( SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {
    @Autowired private DataSource dataSource;
    @Autowired private JdbcTemplate jdbcTemplate;
    @Autowired private EntityManager entityManager;
    @Autowired private TestEntityManager testEntityManager;
    @Autowired private UserRepository userRepository;

    @Test
    public void injectedComponentsAreNotNull(){
        Assert.assertNotEquals(dataSource,null);
        Assert.assertNotEquals(jdbcTemplate,null);
        Assert.assertNotEquals(entityManager,null);
        Assert.assertNotEquals(testEntityManager, null);
        Assert.assertNotEquals(userRepository, null);
    }

    @Test
    public void testFindByUserName(){
        User user = new User();
        String userName = "test";
        String password = "testPassword";
        long id = 1;
        user.setUsername(userName);
        user.setPassword(password);
        user.setId(id);

        userRepository.save(user);

        User result = userRepository.findByUsername(userName);
        Assert.assertEquals(result.getId(),id);
        Assert.assertEquals(result.getPassword(),password);
        Assert.assertEquals(result.getUsername(), userName);
    }
}
