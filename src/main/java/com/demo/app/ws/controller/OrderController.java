package com.demo.app.ws.controller;


import com.demo.app.ws.entities.Order;
import com.demo.app.ws.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/{userId}/orders")
    public ResponseEntity<Order> createOrder(@PathVariable Long userId, @RequestBody Order order) {

        Order createdOrder = orderService.createOrder(userId, order);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }

    @GetMapping("/{userId}/orders/{orderId}")
    public Order getOrder(@PathVariable Long userId, @PathVariable Long orderId) {

        return orderService.getOrder(userId, orderId);
    }

    @PutMapping("/{userId}/orders/{orderId}")
    public Order updateOrder(@PathVariable Long userId, @PathVariable Long orderId, @RequestBody Order order) {
        return orderService.updateOrder(userId, orderId, order);
    }

    @PatchMapping("/{userId}/orders/{orderId}")
    public Order patchUpdateOrder(@PathVariable Long userId, @PathVariable Long orderId, @RequestBody Order order) {
        return orderService.patchUpdateOrder(userId, orderId, order);
    }

    @GetMapping("/{userId}/orders")
    public List<Order> getAllOrders(@PathVariable Long userId) {
        return orderService.getAllOrdersByUserId(userId);
    }

    @DeleteMapping("/{userId}/orders/{orderId}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long userId, @PathVariable Long orderId) {
        orderService.deleteOrder(userId, orderId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
