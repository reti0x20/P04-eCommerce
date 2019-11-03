package com.example.demo.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.example.demo.GenerateTestEnity;
import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

public class ItemControllerTest {

    private ItemController itemController;

    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp(){
        itemController = new ItemController();
        TestUtils.injectObject(itemController, "itemRepository",itemRepository);
    }


    @Test
    public void getItemsTest(){
        List<Item> itemsList = new ArrayList<>();
        itemsList.add(GenerateTestEnity.generateOneItem());
        when(itemRepository.findAll()).thenReturn(itemsList);

        final ResponseEntity<List<Item>> respon = itemController.getItems();
        assertNotNull(respon);
        assertEquals(200,respon.getStatusCodeValue());
        List<Item> items = respon.getBody();
        Item result = items.get(0);
        assertEquals(new Long(0),result.getId());
        assertEquals("test", result.getName());
        assertEquals(new BigDecimal(200.00),result.getPrice());
        assertEquals("description",result.getDescription());
    }

    @Test
    public void getItemByIdTest(){
        Optional<Item> optional = Optional.of(GenerateTestEnity.generateOneItem());
        when(itemRepository.findById(new Long(0))).thenReturn(optional);

        final ResponseEntity<Item> respon = itemController.getItemById(new Long(0));
        assertNotNull(respon);
        assertEquals(200,respon.getStatusCodeValue());
        Item result = respon.getBody();
        assertEquals(new Long(0),result.getId());
        assertEquals("test", result.getName());
        assertEquals("description",result.getDescription());
        assertEquals(new BigDecimal(200.00), result.getPrice());
    }

    @Test
    public void getItemsByName(){
        List<Item> itemList = new ArrayList<>();
        itemList.add(GenerateTestEnity.generateOneItem());
        when(itemRepository.findByName("test")).thenReturn(itemList);

        final ResponseEntity<List<Item>> respon = itemController.getItemsByName("test");

        assertEquals(200, respon.getStatusCodeValue());
        List<Item> result = respon.getBody();
        assertNotEquals(0,result.size());
        Item item = result.get(0);
        assertNotNull(item);
        assertEquals(new Long(0),item.getId());
        assertEquals("test", item.getName());
        assertEquals("description",item.getDescription());
        assertEquals(new BigDecimal(200.00), item.getPrice());
    }
}
