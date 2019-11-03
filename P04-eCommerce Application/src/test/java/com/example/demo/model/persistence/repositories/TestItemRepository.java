package com.example.demo.model.persistence.repositories;

import com.example.demo.model.persistence.Item;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
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

@RunWith( SpringRunner.class )
@DataJpaTest
public class TestItemRepository {
    @Autowired private DataSource dataSource;
    @Autowired private JdbcTemplate jdbcTemplate;
    @Autowired private EntityManager entityManager;
    @Autowired private TestEntityManager testEntityManager;
    @Autowired private ItemRepository itemRepository;

    @Test
    public void injectedComponentsAreNotNull(){
        Assert.assertNotEquals(dataSource,null);
        Assert.assertNotEquals(jdbcTemplate,null);
        Assert.assertNotEquals(entityManager,null);
        Assert.assertNotEquals(testEntityManager, null);
        Assert.assertNotEquals(itemRepository, null);
    }

    //public List<Item> findByName(String name);
    @Test
    public void testFindByName(){
        List<Item> itemList = new ArrayList<>();
        Item item = new Item();
        Long id = new Long(1);
        String name = "test";
        BigDecimal price = new BigDecimal(99.1);
        String decription = "This is test.";
        item.setId(id);
        item.setName(name);
        item.setPrice(price);
        item.setDescription(decription);

        itemRepository.save(item);

        List<Item> result = itemRepository.findByName(name);
        Assert.assertEquals(result.get(0).getName(), name);
        Assert.assertEquals(result.get(0).getId(),id);
        Assert.assertEquals(result.get(0).getPrice(),price);
        Assert.assertEquals(result.get(0).getDescription(),decription);

    }
}
