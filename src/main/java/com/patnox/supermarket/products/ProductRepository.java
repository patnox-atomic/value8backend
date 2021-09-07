package com.patnox.supermarket.products;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.*;
import org.springframework.data.jpa.repository.*;

import java.util.*;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{
	
	@Query("SELECT s FROM products s WHERE s.barcode = ?1")
	Optional<Product> findProductByBarCode(String barcode);

}
