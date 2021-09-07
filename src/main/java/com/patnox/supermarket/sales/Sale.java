package com.patnox.supermarket.sales;

import com.patnox.supermarket.products.*;
import javax.persistence.*;
import java.util.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.DynamicInsert;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.*;

import java.time.*;
import org.apache.commons.lang3.builder.*;

@DynamicInsert
//@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity(name = "sales")
@Table(name = "sales")
public class Sale 
{
	  @Id
	  @Column(name = "id", unique = true, nullable = false)
	  @SequenceGenerator(
		  name = "sale_sequence",
		  sequenceName = "sale_sequence",
		  allocationSize = 1
	  )
	  @GeneratedValue(
			  strategy = GenerationType.SEQUENCE,
			  generator = "sale_sequence"
	  )
	  private Long id;
	  
	  //@NotNull
	  //@Column(name = "product_id")
	  //private Long product_id;
	  //@ManyToOne(optional = false)
	  //@OneToOne(cascade = CascadeType.MERGE)
	  @OneToOne(fetch = FetchType.EAGER,
        cascade = {
                CascadeType.MERGE,
                CascadeType.REFRESH
            })
	  @JoinColumn(name = "product_id", nullable = false, referencedColumnName = "id")
	  private Product product;
	  //private Long product_id;
	  
	  @NotNull
	  @Column(name = "quantity")
	  private Long quantity;
	
	  @NotNull
	  @Min(1)
	  @Column(name = "price")
	  private Double price;
	  
	  @Column(name = "sale_date", columnDefinition="DATE DEFAULT CURRENT_DATE")
	  @JsonFormat(pattern="yyyy-MM-dd", timezone="Africa/Nairobi")
	  //@Temporal(TemporalType.DATE)
	  private LocalDate sale_date;
	  
	  @Column(name = "is_deleted" ,columnDefinition = "boolean default false")
	  private Boolean is_deleted;
	  
	  public Sale() {}
	  
	  public Sale(Long id, Product product, @NotNull Long quantity, @NotNull @Min(1) Double price, LocalDate sale_date,
				Boolean is_deleted) {
			super();
			this.id = id;
			this.product = product;
			this.quantity = quantity;
			this.price = price;
			this.sale_date = sale_date;
			this.is_deleted = is_deleted;
		}
	  

		public Sale(Product product, @NotNull Long quantity, @NotNull @Min(1) Double price, LocalDate sale_date,
			Boolean is_deleted) {
			super();
			this.product = product;
			this.quantity = quantity;
			this.price = price;
			this.sale_date = sale_date;
			this.is_deleted = is_deleted;
		}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Boolean getIs_deleted() {
		return is_deleted;
	}

	public void setIs_deleted(Boolean is_deleted) {
		this.is_deleted = is_deleted;
	}

	public Long getProduct_id() {
		//return product_id;
		if(product != null)
		{
			return product.getId();
		}
		else
		{
			return(-1L);
		}
	}

	public void setProduct_id(Long product_id) {
		//this.product_id = product_id;
		if(product != null)
		{
			product.setId(product_id);
		}
		else
		{
			product = new Product();
			product.setId(product_id);
		}
	}
	

	public Double getPrice() {
		return price;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public LocalDate getSale_date() {
		return sale_date;
	}

	public void setSale_date(LocalDate sale_date) {
		this.sale_date = sale_date;
	}

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		return "Sale [id=" + id + ", quantity=" + quantity + ", price=" + price + ", sale_date=" + sale_date
				+ ", is_deleted=" + is_deleted + ", product_id=" + getProduct_id() + "]";
	}

//	public String toString() 
//	{
//		   return ToStringBuilder.reflectionToString(this);
//	}
	
	
		
}
