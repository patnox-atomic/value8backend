package com.patnox.supermarket.products;

import org.springframework.stereotype.Service;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.patnox.supermarket.orders.Order;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.*;
import java.time.*;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

@Service
public class ProductService 
{
	private final ProductRepository productRepository;

	  @Autowired
	  public ProductService(ProductRepository productRepository) {
	    this.productRepository = productRepository;
	  }
	  
	public List<Product> getAllProducts() 
	{
	    return productRepository.findAll();
	}
	
	//Get a specific product
	public Product getProduct(Long productId) 
	{
	    return productRepository.findById(productId).orElseThrow(() -> new IllegalStateException("Product with ID: " + productId + " does not exist"));
	}
	
	public void addNewProduct(Product newProduct)
	{
		//We enforce that the barcode should be unique
		System.out.println("My New Product: " + newProduct);
		Optional<Product> productByBarcode = productRepository.findProductByBarCode(newProduct.getBarcode());
		if(productByBarcode.isPresent())
		{
			throw new IllegalStateException("Barcode Taken");
		}
		else
		{
			productRepository.save(newProduct);
		}
	}
	
	@Transactional
	public void deleteProduct(Long productId)
	{
		System.out.println("Request to delete Product ID: " + productId);
		boolean exists = productRepository.existsById(productId);
		if(!exists)
		{
			System.err.println("Error: Product with ID: " + productId + " does not exist");
			throw new IllegalStateException("Product with ID: " + productId + " does not exist");
		}
		else
		{
			System.out.println("Product with ID: " + productId + " exists so we will proceed");
			Product victimizedProduct = productRepository.findById(productId).orElseThrow(() -> new IllegalStateException("Product with ID: " + productId + " does not exist"));
			victimizedProduct.setIs_deleted(true);
		}
	}
	
	@Transactional
	public void updateProduct(Long productId, String name, String description, String barcode, Long quantity, Double price, Long reorder_level, Long reorder_quantity, Boolean is_deleted)
	{
		System.out.println("Request to update Product ID: " + productId);
		boolean exists = productRepository.existsById(productId);
		if(!exists)
		{
			System.err.println("Error: Product with ID: " + productId + " does not exist");
			throw new IllegalStateException("Product with ID: " + productId + " does not exist");
		}
		else
		{
			System.out.println("Product with ID: " + productId + " exists so we will proceed");
			Product victimizedProduct = productRepository.findById(productId).orElseThrow(() -> new IllegalStateException("Product with ID: " + productId + " does not exist"));
			if(name != null && name.length() > 0) victimizedProduct.setName(name);
			if(description != null && description.length() > 0) victimizedProduct.setDescription(description);
			if(barcode != null && barcode.length() > 0) victimizedProduct.setBarcode(barcode);
			if(quantity != null && quantity != 0) victimizedProduct.setQuantity(quantity);
			if(price != null && price != 0.00) victimizedProduct.setPrice(price);
			if(reorder_quantity != null && reorder_quantity != 0) victimizedProduct.setReorder_quantity(reorder_quantity);
			if(reorder_level != null && reorder_level != 0) victimizedProduct.setReorder_quantity(reorder_level);
			if(is_deleted != null) victimizedProduct.setIs_deleted(is_deleted);
		}
	}
}
