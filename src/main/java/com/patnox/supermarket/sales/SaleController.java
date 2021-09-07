package com.patnox.supermarket.sales;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.time.*;

@RestController
@RequestMapping(path = "api/v1/sale")
public class SaleController {
	
	private final SaleService saleService;
		
	@Autowired
	public SaleController(SaleService saleService) {
		this.saleService = saleService;
	}

	@GetMapping
	public List<Sale> getAll() {
		return saleService.getAllSales();
	}
	
	@PostMapping
	public void createNewSale(@RequestBody Sale newSale)
	{
		saleService.addNewSale(newSale);
	}
	
	@DeleteMapping(path = "{saleId}")
	public void deleteSale(@PathVariable("saleId") Long saleId)
	{
		saleService.deleteSale(saleId);
	}
	
	@PutMapping(path = "{saleId}")
	public void deleteSale(@PathVariable("saleId") Long saleId,
				@RequestParam(required = false) Long product_id,
				@RequestParam(required = false) Long quantity,
				@RequestParam(required = false) Double price,
				@RequestParam(required = false) String sale_date,
				@RequestParam(required = false) Boolean is_deleted
			)
	{
		saleService.updateSale(saleId, product_id, quantity, price, sale_date, is_deleted);
	}
	
}
