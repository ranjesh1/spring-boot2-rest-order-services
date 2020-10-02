package com.demo.app.ws.repository;

import com.demo.app.ws.entities.Order;
import com.demo.app.ws.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void setup() {
    }

    @Test
    void testFndAllByUser() {
        //Given
        User user = new User();
        user.setFirstName("Steve");
        user.setLastName("Rob");
        user.setEmail("steverob@gmail.com");
        user.setFirstLineOfAddress("10 Oliver");
        user.setSecondLineOfAddress("Commercial street");
        user.setTown("London");
        user.setPostCode("HA4 7EW");
        entityManager.persist(user);
        entityManager.flush();

        Order order = new Order();
        order.setUser(user);
        order.setCompletedStatus(false);
        order.setDescription("Apple Phone");
        order.setPriceInPence(10000L);
        entityManager.persist(order);
        entityManager.flush();

        Order order2 = new Order();
        order2.setUser(user);
        order2.setCompletedStatus(false);
        order2.setDescription("Samsung galaxy Phone");
        order2.setPriceInPence(50000L);

        entityManager.persist(order2);
        entityManager.flush();

        //When
        List<Order> allOrders = orderRepository.findAllByUser(user);
        //Then
        assertTrue(allOrders.size() > 0);
        assertEquals(order.getDescription(), allOrders.get(0).getDescription());
        assertEquals(order.getPriceInPence(), allOrders.get(0).getPriceInPence());
    }

    @Test
    void testFindByIdAndUserId() {
        //Given
        User user = new User();
        user.setFirstName("Steve");
        user.setLastName("Rob");
        user.setEmail("steverob@gmail.com");
        user.setFirstLineOfAddress("10 Oliver");
        user.setSecondLineOfAddress("Commercial street");
        user.setTown("London");
        user.setPostCode("HA4 7EW");
        entityManager.persist(user);
        entityManager.flush();

        Order order = new Order();
        order.setUser(user);
        order.setCompletedStatus(false);
        order.setDescription("Apple Phone");
        order.setPriceInPence(10000L);
        entityManager.persist(order);
        entityManager.flush();

        //When
        Order orderFound = orderRepository.findByIdAndUserId(order.getId(), user.getId()).get();
        //Then
        assertNotNull(orderFound);
        assertEquals(order.getDescription(), orderFound.getDescription());
        assertEquals(order.getPriceInPence(), orderFound.getPriceInPence());
        assertEquals(order.getUser().getId(), orderFound.getUser().getId());
    }

}
