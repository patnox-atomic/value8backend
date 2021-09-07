package com.patnox.supermarket.products;

import java.text.SimpleDateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.*;
import org.springframework.stereotype.*;
import java.util.*;
import java.text.*;
import com.patnox.supermarket.orders.*;

@Component
public class ScheduledTaskCheckReorderLevels 
{
	private static final Logger log = LoggerFactory.getLogger(ScheduledTaskCheckReorderLevels.class);
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	private final ProductRepository productRepository;
	private final OrderRepository orderRepository;
	
	@Autowired
	public ScheduledTaskCheckReorderLevels(ProductRepository productRepository, OrderRepository orderRepository) {
		this.productRepository = productRepository;
		this.orderRepository = orderRepository;
	}

	//check after every 30 seconds
	@Scheduled(fixedRate = 30000)
	public void checkProductReorderLevels() {
		log.info("Checking Product Reorder Levels. The time is now {}", dateFormat.format(new Date()));
		//get all products
		List<Product> products = productRepository.findAll();
		//check reorder levels and create orders if no orders exist
		for (Product i : products) {
			long level = i.getReorder_level();
			long quantity = i.getQuantity();
			if(quantity < level)
			{
				log.info("We got a hit.Product needs a reorder. Product: {}", i.getName());
				//check if an unfulfilled order exists
				List<Order> orders = orderRepository.findUnfulfilledOrderByProduct(i.getId());
				if(orders != null)
				{
					if(orders.size() > 0)
					{
						log.info("We got some unfullfilled orders. We ignore. Product: {}", i.getName());
					}
					else
					{
						log.info("We have NO unfullfilled orders. We need to create a new order. Product: {}", i.getName());
						Order newOrder = new Order();
						long reorderQuantity = i.getReorder_quantity();
						long prodId = i.getId();
						newOrder.setProduct_id(prodId);
						newOrder.setQuantity(reorderQuantity);
						orderRepository.save(newOrder);
					}
				}
				else
				{
					log.info("We have NO unfullfilled orders. We need to create a new order. Product: {}", i.getName());
					Order newOrder = new Order();
					long reorderQuantity = i.getReorder_quantity();
					long prodId = i.getId();
					newOrder.setProduct_id(prodId);
					newOrder.setQuantity(reorderQuantity);
					orderRepository.save(newOrder);
				}
			}
		}
	}
}
