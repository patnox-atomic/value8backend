package com.patnox.supermarket.products;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.patnox.supermarket.orders.Order;

import java.util.*;
import java.time.*;

@RestController
@RequestMapping(path = "api/v1/product")
public class ProductController {
	
	private final ProductService productService;
		
	@Autowired
	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	@GetMapping
	public List<Product> getAll() {
		return productService.getAllProducts();
	}
	
	@GetMapping(path = "{productId}")
	public Product getProduct(@PathVariable("productId") Long productId) {
		return productService.getProduct(productId);
	}
	
	@PostMapping
	public void createNewOrder(@RequestBody Product newProduct)
	{
		productService.addNewProduct(newProduct);
	}
	
	@DeleteMapping(path = "{productId}")
	public void deleteProduct(@PathVariable("productId") Long productId)
	{
		productService.deleteProduct(productId);
	}
	
	@PutMapping(path = "{productId}")
	public void updateProduct(@PathVariable("productId") Long productId,
				@RequestParam(required = false) String name,
				@RequestParam(required = false) String description,
				@RequestParam(required = false) String barcode,
				@RequestParam(required = false) Double price,
				@RequestParam(required = false) Long quantity,
				@RequestParam(required = false) Long reorder_level,
				@RequestParam(required = false) Long reorder_quantity,
				@RequestParam(required = false) Boolean is_deleted
			)
	{
		productService.updateProduct(productId, name, description, barcode, quantity, price, reorder_level, reorder_quantity, is_deleted);
	}
	
}
