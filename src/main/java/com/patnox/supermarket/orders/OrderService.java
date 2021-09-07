package com.patnox.supermarket.orders;

import org.springframework.stereotype.Service;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.*;
import java.time.*;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import com.patnox.supermarket.products.*;

@Service
public class OrderService 
{
	private final OrderRepository orderRepository;
	private final ProductRepository productRepository;

	  @Autowired
	  public OrderService(OrderRepository orderRepository, ProductRepository productRepository) {
	    this.orderRepository = orderRepository;
	    this.productRepository = productRepository;
	  }
	  
	public List<Order> getAllOrders() 
	{
	    return orderRepository.findAll();
	}
	
	//Get a specific order
	public Order getOrder(Long orderId) 
	{
	    return orderRepository.findById(orderId).orElseThrow(() -> new IllegalStateException("Order with ID: " + orderId + " does not exist"));
	}
	
	@Transactional
	public void fullfillOrder(Long orderId)
	{
		boolean orderExists = orderRepository.existsById(orderId);
		if(!orderExists)
		{
			System.err.println("Error: Order with ID: " + orderId + " does not exist");
			throw new IllegalStateException("Order with ID: " + orderId + " does not exist");
		}
		else
		{
			System.out.println("Order with ID: " + orderId + " exists so we will proceed");
			//search for the order by id and get order quantity and product id
			Order selectedOrder = orderRepository.findById(orderId).orElseThrow(() -> new IllegalStateException("Order with ID: " + orderId + " does not exist"));
			//check if order has already been fullfilled
			if(!selectedOrder.getIs_fullfilled())
			{
				Long productId = selectedOrder.getProduct_id();
				long orderQuantity = selectedOrder.getQuantity();
				//mark order as fullfilled
				selectedOrder.setIs_fullfilled(true);
				//search for the product by id
				boolean productExists = productRepository.existsById(productId);
				if(!productExists)
				{
					System.err.println("Error: Product with ID: " + productId + " does not exist");
					throw new IllegalStateException("Product with ID: " + productId + " does not exist");
				}
				else
				{
					System.out.println("Product with ID: " + productId + " exists so we will proceed");
					Product selectedProduct = productRepository.findById(productId).orElseThrow(() -> new IllegalStateException("Product with ID: " + productId + " does not exist"));
					//increament product quantity
					Long currentProductQuantity = selectedProduct.getQuantity();
					selectedProduct.setQuantity((currentProductQuantity + orderQuantity));
				}
			}
			else
			{
				System.err.println("Error: Order with ID: " + orderId + " is already fullfilled");
				throw new IllegalStateException("Order with ID: " + orderId + " is already fullfilled");
			}
		}
	}
	
	public void addNewOrder(Order newOrder)
	{
		System.out.println("My New Order: " + newOrder);
		orderRepository.save(newOrder);
	}
	
	@Transactional
	public void deleteOrder(Long orderId)
	{
		System.out.println("Request to delete Order ID: " + orderId);
		boolean exists = orderRepository.existsById(orderId);
		if(!exists)
		{
			System.err.println("Error: Order with ID: " + orderId + " does not exist");
			throw new IllegalStateException("Order with ID: " + orderId + " does not exist");
		}
		else
		{
			System.out.println("Order with ID: " + orderId + " exists so we will proceed");
			Order victimizedOrder = orderRepository.findById(orderId).orElseThrow(() -> new IllegalStateException("Order with ID: " + orderId + " does not exist"));
			victimizedOrder.setIs_deleted(true);
		}
	}
	
	@Transactional
	public void updateOrder(Long orderId, Long product_id, Long quantity, Boolean is_fullfilled, String date_ordered, String date_fullfilled, Boolean is_deleted)
	{
		System.out.println("Request to update Order ID: " + orderId);
		boolean exists = orderRepository.existsById(orderId);
		if(!exists)
		{
			System.err.println("Error: Order with ID: " + orderId + " does not exist");
			throw new IllegalStateException("Order with ID: " + orderId + " does not exist");
		}
		else
		{
			System.out.println("Order with ID: " + orderId + " exists so we will proceed");
			Order victimizedOrder = orderRepository.findById(orderId).orElseThrow(() -> new IllegalStateException("Order with ID: " + orderId + " does not exist"));
			Product victimizedProduct = productRepository.findById(product_id).orElseThrow(() -> new IllegalStateException("Product with ID: " + product_id + " does not exist"));
			if(product_id != null && product_id != 0) victimizedOrder.setProduct(victimizedProduct);
			if(quantity != null && quantity != 0) victimizedOrder.setQuantity(quantity);
			if(is_fullfilled != null) victimizedOrder.setIs_fullfilled(is_fullfilled);
			if(product_id != null && date_ordered.length() > 0) victimizedOrder.setDate_ordered(LocalDate.parse(date_ordered, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
			if(date_fullfilled != null && date_fullfilled.length() > 0) victimizedOrder.setDate_fullfilled(LocalDate.parse(date_fullfilled, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
			if(is_deleted != null) victimizedOrder.setIs_deleted(is_deleted);
		}
	}
}
