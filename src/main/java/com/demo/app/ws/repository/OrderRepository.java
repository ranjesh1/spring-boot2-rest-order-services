package com.demo.app.ws.repository;

import com.demo.app.ws.entities.Order;
import com.demo.app.ws.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends CrudRepository<Order, Long> {
    Optional<Order> findByIdAndUserId(Long id, Long userId);

    List<Order> findAllByUser(User user);
}
