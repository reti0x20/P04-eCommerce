package com.example.demo.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.example.demo.GenerateTestEnity;
import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

public class CartControllerTest {
    private CartController cartController;

    private UserRepository userRepository = mock(UserRepository.class);

    private CartRepository cartRepository = mock(CartRepository.class);

    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp(){
        cartController = new CartController();
        TestUtils.injectObject(cartController,"userRepository",userRepository);
        TestUtils.injectObject(cartController,"cartRepository",cartRepository);
        TestUtils.injectObject(cartController,"itemRepository",itemRepository);
    }

    @Test
    public void addTocartTest(){
        when(userRepository.findByUsername("test")).thenReturn(GenerateTestEnity.generateOneUser());
        when(itemRepository.findById(new Long(0))).thenReturn(Optional.of(GenerateTestEnity.generateOneItem()));

        ModifyCartRequest request = new ModifyCartRequest();
        request.setItemId(0);
        request.setQuantity(1);
        request.setUsername("test");

        final ResponseEntity<Cart> respon = cartController.addTocart(request);
        assertEquals(200, respon.getStatusCodeValue());
        Cart cart = respon.getBody();
        assertNotNull(cart);
        assertEquals(new Long(0),cart.getId());
        assertEquals(new BigDecimal(1200.00),cart.getTotal());
        assertNotNull(cart.getItems());
        assertEquals(2,cart.getItems().size());
        assertEquals(new Long(0),cart.getItems().get(0).getId());
        assertNotNull(cart.getUser());
        assertEquals("test", cart.getUser().getUsername());
    }

    @Test
    public void removeFromcart(){
        when(userRepository.findByUsername("test")).thenReturn(GenerateTestEnity.generateOneUser());
        when(itemRepository.findById(new Long(0))).thenReturn(Optional.of(GenerateTestEnity.generateOneItem()));

        ModifyCartRequest request = new ModifyCartRequest();
        request.setItemId(0);
        request.setQuantity(1);
        request.setUsername("test");

        final ResponseEntity<Cart> respon = cartController.removeFromcart(request);
        assertEquals(200, respon.getStatusCodeValue());
        Cart cart = respon.getBody();
        assertNotNull(cart);
        assertEquals(new Long(0),cart.getId());
        assertEquals(new BigDecimal(800.00),cart.getTotal());
        assertNotNull(cart.getUser());
        assertEquals("test",cart.getUser().getUsername());
        assertNotNull(cart.getItems());
        assertEquals(0,cart.getItems().size());
    }
}
