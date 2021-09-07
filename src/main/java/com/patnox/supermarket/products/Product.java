package com.patnox.supermarket.products;

import javax.persistence.*;
import java.util.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.constraints.*;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.*;

import java.time.*;
import org.apache.commons.lang3.builder.*;

@DynamicInsert
//@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity(name = "products")
@Table(name = "products")
public class Product 
{
	  @Id
	  @Column(name = "id", unique = true, nullable = false)
	  @SequenceGenerator(
		  name = "product_sequence",
		  sequenceName = "product_sequence",
		  allocationSize = 1
	  )
	  @GeneratedValue(
			  strategy = GenerationType.SEQUENCE,
			  generator = "product_sequence"
	  )
	  private Long id;
	  
	  @NotBlank
	  @NotNull
	  @Column(name = "name", length = 254)
	  @Size(min = 1, max = 254)
	  @Length(min = 1, max = 254)
	  private String name;
	  
	  @NotBlank
	  @NotNull
	  @Column(name = "description", length = 254)
	  @Size(min = 1, max = 254)
	  @Length(min = 1, max = 254)
	  private String description;
	  
	  @NotBlank
	  @NotNull
	  @Column(name = "barcode", length = 254, unique = true, nullable = false)
	  @Size(min = 1, max = 254)
	  @Length(min = 1, max = 254)
	  private String barcode;
	
	  @NotNull
	  @Min(1)
	  @Column(name = "price")
	  private Double price;
	  
	  @NotNull
	  @Min(1)
	  @Column(name = "quantity")
	  private Long quantity;
	  
	  @NotNull
	  @Min(1)
	  @Column(name = "reorder_level")
	  private Long reorder_level;
	
	  @NotNull
	  @Min(1)
	  @Column(name = "reorder_quantity")
	  private Long reorder_quantity;
	  
	  @Column(name = "is_deleted" ,columnDefinition = "boolean default false")
	  private Boolean is_deleted;
	  
	  public Product() {}

	public Product(Long id, @NotBlank @NotNull @Size(min = 1, max = 254) @Length(min = 1, max = 254) String name,
			@NotBlank @NotNull @Size(min = 1, max = 254) @Length(min = 1, max = 254) String description,
			@NotBlank @NotNull @Size(min = 1, max = 254) @Length(min = 1, max = 254) String barcode,
			@NotNull @Min(1) Double price, @NotNull Long quantity, @NotNull @Min(1) Long reorder_level,
			@NotNull @Min(1) Long reorder_quantity, Boolean is_deleted) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.barcode = barcode;
		this.price = price;
		this.quantity = quantity;
		this.reorder_level = reorder_level;
		this.reorder_quantity = reorder_quantity;
		this.is_deleted = is_deleted;
	}

	public Product(@NotBlank @NotNull @Size(min = 1, max = 254) @Length(min = 1, max = 254) String name,
			@NotBlank @NotNull @Size(min = 1, max = 254) @Length(min = 1, max = 254) String description,
			@NotBlank @NotNull @Size(min = 1, max = 254) @Length(min = 1, max = 254) String barcode,
			@NotNull @Min(1) Double price, @NotNull Long quantity, @NotNull @Min(1) Long reorder_level,
			@NotNull @Min(1) Long reorder_quantity, Boolean is_deleted) {
		super();
		this.name = name;
		this.description = description;
		this.barcode = barcode;
		this.price = price;
		this.quantity = quantity;
		this.reorder_level = reorder_level;
		this.reorder_quantity = reorder_quantity;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Long getReorder_level() {
		return reorder_level;
	}

	public void setReorder_level(Long reorder_level) {
		this.reorder_level = reorder_level;
	}

	public Long getReorder_quantity() {
		return reorder_quantity;
	}

	public void setReorder_quantity(Long reorder_quantity) {
		this.reorder_quantity = reorder_quantity;
	}

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	public String toString() 
	{
		   return ToStringBuilder.reflectionToString(this);
	}
		
}
