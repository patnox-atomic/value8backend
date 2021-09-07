package com.patnox.supermarket.orders;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.patnox.supermarket.products.*;

import java.util.*;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>
{
	@Query("SELECT s FROM orders s WHERE s.product.id = ?1")
	Optional<Order> findOrderByProduct(long id);
	
	@Query("SELECT s FROM orders s WHERE s.product.id = ?1 and is_fullfilled = false")
	//Optional<Order> findUnfulfilledOrderByProduct(long id);
	List<Order> findUnfulfilledOrderByProduct(long id);
}
