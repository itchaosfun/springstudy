package com.chaos.taco.repository;

import com.chaos.taco.bean.Order;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order,Long> {
}
