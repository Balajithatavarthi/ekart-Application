package com.infosys.infytel.repository;


import org.springframework.data.jpa.repository.JpaRepository; 

import com.infosys.infytel.entity.Order;

public interface OrderRepository extends JpaRepository<Order, String> {
		Order findTopByOrderByOrderIdDesc();
}
