package com.patnox.supermarket.sales;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long>{

}
