package com.example.demo.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.example.demo.GenerateTestEnity;
import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

public class OrderControllerTest {

    private OrderController orderController ;

    private UserRepository userRepository = mock(UserRepository.class);

    private OrderRepository orderRepository = mock(OrderRepository.class);

    @Before
    public void setUp(){
        orderController = new OrderController();
        TestUtils.injectObject(orderController,"userRepository", userRepository);
        TestUtils.injectObject(orderController,"orderRepository", orderRepository);
    }

    @Test
    public void submitTest(){
        when(userRepository.findByUsername("test")).thenReturn(GenerateTestEnity.generateOneUser());
        ResponseEntity<UserOrder> respon = orderController.submit("test");

        assertEquals(200,respon.getStatusCodeValue());
        UserOrder result = respon.getBody();
        assertNotNull(result);
        assertEquals(new BigDecimal(1000.00),result.getTotal());
        assertNotNull(result.getUser());
        assertEquals("test",result.getUser().getUsername());
        assertEquals("thisIsHashed",result.getUser().getPassword());
        assertEquals(0,result.getUser().getId());
        assertNotNull(result.getItems());
        assertEquals(new Long(0),result.getItems().get(0).getId());
        assertEquals("test",result.getItems().get(0).getName());
        assertEquals("description",result.getItems().get(0).getDescription());
        assertEquals(new BigDecimal(200.00),result.getItems().get(0).getPrice());
    }

    @Test
    public void getOrdersForUser(){
        User user = GenerateTestEnity.generateOneUser();
        when(userRepository.findByUsername("test")).thenReturn(user);

        UserOrder order = GenerateTestEnity.generateOneUserOrder();
        List<UserOrder> orderList = new ArrayList<>();
        orderList.add(order);
        when(orderRepository.findByUser(user)).thenReturn(orderList);

        ResponseEntity<List<UserOrder>> respon = orderController.getOrdersForUser("test");

        assertEquals(200,respon.getStatusCodeValue());
        System.out.println(respon.getBody().size());

        List<UserOrder> orders = respon.getBody();
        assertNotEquals(0, orders.size());
        assertEquals(new Long(0),orders.get(0).getId());
        assertEquals(new BigDecimal(9999.99), orders.get(0).getTotal());

        assertNotNull(orders.get(0).getUser());
        assertNotNull(orders.get(0).getUser().getCart());
        assertEquals(0, orders.get(0).getUser().getId());
        assertEquals("thisIsHashed", orders.get(0).getUser().getPassword());
        assertEquals("test",orders.get(0).getUser().getUsername());

        assertNotEquals(0,orders.get(0).getItems().size());
        assertEquals(new Long(0),orders.get(0).getItems().get(0).getId());
        assertEquals("test",orders.get(0).getItems().get(0).getName());
        assertEquals(new BigDecimal(200.00),orders.get(0).getItems().get(0).getPrice());
        assertEquals("description",orders.get(0).getItems().get(0).getDescription());
    }


}
