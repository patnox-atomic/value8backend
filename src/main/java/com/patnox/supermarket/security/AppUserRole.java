package com.patnox.supermarket.security;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//public enum AppUserRole {
//	USER_ROLE,
//    ADMIN_ROLE,
//    MANAGER_ROLE,
//    SUPERUSER_ROLE,
//    MAINTENANCE_ROLE,
//    AUDITOR_ROLE,
//    WAREHOUSE_ATTENDANT_ROLE,
//    RETAIL_ATTENDANT_ROLE
//}

@Entity(name = "userroles")
@Table(name = "userroles")
@Data
@DynamicInsert
@NoArgsConstructor
@AllArgsConstructor
public class AppUserRole 
{
		@Id
		@Column(name = "id", unique = true, nullable = false)
		@SequenceGenerator(
			name = "appuserrole_sequence",
			sequenceName = "appuserrole_sequence",
			allocationSize = 1
		)
		@GeneratedValue(
			strategy = GenerationType.SEQUENCE,
			generator = "appuserrole_sequence"
		)
		private Long id;
		
		@Column(name = "name", unique = true, nullable = false)
		private String name;
		
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		
		public AppUserRole(String name) {
			super();
			this.name = name;
		}
		
}
		
