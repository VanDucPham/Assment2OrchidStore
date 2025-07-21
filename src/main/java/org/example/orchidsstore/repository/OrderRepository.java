package org.example.orchidsstore.repository;

import org.example.orchidsstore.Entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByAccount_AccId(Integer accId);
    List<Order> findByOrderStatus(String orderStatus);
}
