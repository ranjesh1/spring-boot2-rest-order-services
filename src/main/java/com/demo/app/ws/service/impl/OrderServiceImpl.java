package com.demo.app.ws.service.impl;

import com.demo.app.ws.entities.Order;
import com.demo.app.ws.entities.User;
import com.demo.app.ws.repository.OrderRepository;
import com.demo.app.ws.repository.UserRepository;
import com.demo.app.ws.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Order createOrder(Long userId, Order order) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User with id " + userId + " Not found"));

        order.setUser(user);

        return orderRepository.save(order);
    }

    @Override
    public Order getOrder(Long userId, Long orderId) {

        return orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new RuntimeException("User with id " + userId + " and order id " + orderId + " Not found"));
    }

    @Override
    public List<Order> getAllOrdersByUserId(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User with id " + userId + " Not found"));

        return orderRepository.findAllByUser(user);

    }

    @Override
    public Order updateOrder(Long userId, Long orderId, Order order) {
        Order orderFound = orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new RuntimeException("User with id " + userId + " and order id " + orderId + " Not found"));

        orderFound.setDescription(order.getDescription());
        orderFound.setPriceInPence(order.getPriceInPence());

        return orderRepository.save(orderFound);
    }

    @Override
    public Order patchUpdateOrder(Long userId, Long orderId, Order order) {

        Order orderFound = orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new RuntimeException("User with id " + userId + " and order id " + orderId + " Not found"));

        if (order.getDescription() != null)
            orderFound.setDescription(order.getDescription());

        if (order.getPriceInPence() != 0)
            orderFound.setPriceInPence(order.getPriceInPence());

        return orderRepository.save(orderFound);

    }

    @Override
    public void deleteOrder(Long userId, Long orderId) {
        Order order = orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new RuntimeException("User with id " + userId + " and order id " + orderId + " Not found"));
        orderRepository.delete(order);
    }
}
