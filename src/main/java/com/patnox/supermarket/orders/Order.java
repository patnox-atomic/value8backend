package com.patnox.supermarket.orders;

import javax.persistence.*;
import java.util.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.DynamicInsert;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.patnox.supermarket.products.Product;

import java.time.*;
import org.apache.commons.lang3.builder.*;

@DynamicInsert
//@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity(name = "orders")
@Table(name = "orders")
public class Order 
{
	  @Id
	  @Column(name = "id", unique = true, nullable = false)
	  @SequenceGenerator(
		  name = "order_sequence",
		  sequenceName = "order_sequence",
		  allocationSize = 1
	  )
	  @GeneratedValue(
			  strategy = GenerationType.SEQUENCE,
			  generator = "order_sequence"
	  )
	  private Long id;
	  
//	  @NotNull
//	  @Column(name = "product_id")
//	  private Long product_id;
	  //@ManyToOne(optional = false)
	  //@OneToOne(cascade = CascadeType.MERGE)
	  @OneToOne(fetch = FetchType.EAGER,
      cascade = {
              CascadeType.MERGE,
              CascadeType.REFRESH
          })
	  @JoinColumn(name = "product_id", nullable = false, referencedColumnName = "id")
	  private Product product;
	
	  @NotNull
	  @Min(1)
	  @Column(name = "quantity")
	  private Long quantity;
	  
	  @Column(name = "is_fullfilled" ,columnDefinition = "boolean default false")
	  private Boolean is_fullfilled;
	  
	  @Column(name = "date_ordered", columnDefinition="DATE DEFAULT CURRENT_DATE")
	  //@Temporal(TemporalType.DATE)
	  @JsonFormat(pattern="yyyy-MM-dd", timezone="Africa/Nairobi")
	  private LocalDate date_ordered;
	  
	  @Column(name = "date_fullfilled", columnDefinition="DATE DEFAULT CURRENT_DATE")
	  //@Temporal(TemporalType.DATE)
	  @JsonFormat(pattern="yyyy-MM-dd", timezone="Africa/Nairobi")
	  private LocalDate date_fullfilled;
	  
	  @Column(name = "is_deleted" ,columnDefinition = "boolean default false")
	  private Boolean is_deleted;
	  
	  public Order() {}
	  
	  public Order(Long id, Product product, @NotNull @Min(1) Long quantity, Boolean is_fullfilled, LocalDate date_ordered,
				LocalDate date_fullfilled, Boolean is_deleted) {
			super();
			this.id = id;
			this.product = product;
			this.quantity = quantity;
			this.is_fullfilled = is_fullfilled;
			this.date_ordered = date_ordered;
			this.date_fullfilled = date_fullfilled;
			this.is_deleted = is_deleted;
		}

		public Order(Product product, @NotNull @Min(1) Long quantity, Boolean is_fullfilled, LocalDate date_ordered,
				LocalDate date_fullfilled, Boolean is_deleted) {
			super();
			this.product = product;
			this.quantity = quantity;
			this.is_fullfilled = is_fullfilled;
			this.date_ordered = date_ordered;
			this.date_fullfilled = date_fullfilled;
			this.is_deleted = is_deleted;
		}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Long getQuantity() {
		return quantity;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	public Boolean getIs_fullfilled() {
		return is_fullfilled;
	}

	public void setIs_fullfilled(Boolean is_fullfilled) {
		this.is_fullfilled = is_fullfilled;
	}

	public LocalDate getDate_ordered() {
		return date_ordered;
	}

	public void setDate_ordered(LocalDate date_ordered) {
		this.date_ordered = date_ordered;
	}

	public LocalDate getDate_fullfilled() {
		return date_fullfilled;
	}

	public void setDate_fullfilled(LocalDate date_fullfilled) {
		this.date_fullfilled = date_fullfilled;
	}
	
	public Boolean getIs_deleted() {
		return is_deleted;
	}

	public void setIs_deleted(Boolean is_deleted) {
		this.is_deleted = is_deleted;
	}

	@Override
	public String toString() {
		return "Order [id=" + id + ", quantity=" + quantity + ", is_fullfilled=" + is_fullfilled + ", date_ordered="
				+ date_ordered + ", date_fullfilled=" + date_fullfilled + ", is_deleted=" + is_deleted
				+ ", product_id=" + getProduct_id() + "]";
	}

//	public String toString() 
//	{
//		   return ToStringBuilder.reflectionToString(this);
//	}
		
}
