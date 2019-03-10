package com.demo.app.ws.controller;

import com.demo.app.ws.entities.Order;
import com.demo.app.ws.entities.User;
import com.demo.app.ws.service.OrderService;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = OrderController.class, secure = false)
class OrderControllerTest {

    @MockBean
    private OrderService orderService;

    @Autowired
    private MockMvc mockMvc;

    private Order order;
    private User user;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        user = new User();
        user.setFirstName("Steve");
        user.setLastName("Rob");
        user.setEmail("steverob@gmail.com");
        user.setId(1L);
        user.setFirstLineOfAddress("11 Avenue");
        user.setSecondLineOfAddress("Commercial street");
        user.setTown("London");
        user.setPostCode("HA7 0EW");

        order = new Order();
        order.setUser(user);
        order.setCompletedStatus(false);
        order.setId(2L);
        order.setDescription("Apple Phone");
        order.setPriceInPence(10000L);
    }

    @Test
    void testGetOrder() throws Exception {
        String expected = objectMapper.writeValueAsString(order);
        when(orderService.getOrder(anyLong(), anyLong())).thenReturn(order);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/api/users/{userId}/orders/{orderId}", user.getId(), order.getId()).accept(
                MediaType.APPLICATION_JSON);
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        assertEquals(expected, mvcResult.getResponse().getContentAsString());
    }

    @Test
    void testGetAllOrders() throws Exception {

        Order order2 = new Order();
        order2.setUser(user);
        order2.setCompletedStatus(false);
        order2.setId(3L);
        order2.setDescription("Samsung Phone");
        order2.setPriceInPence(30000L);

        List<Order> orders = Arrays.asList(order, order2);
        String expected = objectMapper.writeValueAsString(orders);

        when(orderService.getAllOrdersByUserId(anyLong())).thenReturn(orders);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/api/users/{userId}/orders", user.getId()).accept(MediaType.APPLICATION_JSON);
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();


        assertEquals(expected, mvcResult.getResponse().getContentAsString());
    }

    @Test
    void testCreateOrder() throws Exception {

        Order createOrder = new Order();
        createOrder.setDescription(order.getDescription());
        createOrder.setPriceInPence(order.getPriceInPence());

        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        String createOrderString = objectMapper.writeValueAsString(createOrder);
        String expectedOrderString = objectMapper.writeValueAsString(order);

        when(orderService.createOrder(anyLong(), any(Order.class))).thenReturn(order);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/users/{userId}/orders", user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(createOrderString)
                .accept(MediaType.APPLICATION_JSON);


        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        assertEquals(expectedOrderString, mvcResult.getResponse().getContentAsString());

    }

    @Test
    void testUpdateOrder() throws Exception {

        Order updateOrder = new Order();
        updateOrder.setDescription("Updated Apple Phone");
        updateOrder.setPriceInPence(20000L);
        order.setDescription(updateOrder.getDescription());
        order.setPriceInPence(updateOrder.getPriceInPence());

        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        String updateOrderString = objectMapper.writeValueAsString(updateOrder);
        String expectedOrderString = objectMapper.writeValueAsString(order);

        when(orderService.updateOrder(anyLong(), anyLong(), any(Order.class))).thenReturn(order);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put(
                "/api/users/{userId}/orders/{orderId}", user.getId(), order.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateOrderString)
                .accept(MediaType.APPLICATION_JSON);
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        assertEquals(expectedOrderString, mvcResult.getResponse().getContentAsString());
    }

    @Test
    void testPatchUpdateOrder() throws Exception {

        Order updateOrder = new Order();
        updateOrder.setDescription("Updated Apple Phone");
        order.setDescription(updateOrder.getDescription());

        // exclude null fields
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        String updateOrderString = objectMapper.writeValueAsString(updateOrder);
        String expectedOrderString = objectMapper.writeValueAsString(order);

        when(orderService.updateOrder(anyLong(), anyLong(), any(Order.class))).thenReturn(order);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put(
                "/api/users/{userId}/orders/{orderId}", user.getId(), order.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateOrderString)
                .accept(MediaType.APPLICATION_JSON);
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        assertEquals(expectedOrderString, mvcResult.getResponse().getContentAsString());
    }

    @Test
    void testDeleteOrder() throws Exception {
        doNothing().when(orderService).deleteOrder(anyLong(), anyLong());
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/users/{userId}/orders/{orderId}", user.getId(), order.getId())
                .accept(MediaType.APPLICATION_JSON);
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andReturn();
        assertEquals("", mvcResult.getResponse().getContentAsString());

    }

}
