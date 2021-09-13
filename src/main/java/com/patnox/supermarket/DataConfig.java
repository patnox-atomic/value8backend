package com.patnox.supermarket;

import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import java.time.*;
import java.util.*;
import com.patnox.supermarket.orders.*;
import com.patnox.supermarket.products.*;
import com.patnox.supermarket.sales.*;
import com.patnox.supermarket.security.*;

@Configuration
public class DataConfig 
{
	@Bean
    public JavaMailSender javaMailSender() { 
          //return new JavaMailSender();
          return new JavaMailSenderImpl();
    }
	
	@Bean
	CommandLineRunner productDataInjector(ProductRepository productRepository, OrderRepository orderRepository, SaleRepository saleRepository, 
			AppUserService userService, AppUserRoleRepository appUserRoleRepository)
	{
		return args -> {
			//Sample products
			Product prd1 = new Product(
					"SuperLoaf",
					"Best Bread Ever",
					"37827719384",
					732.00D,
					500L,
					30L,
					600L,
					false
			);
			Product prd2 = new Product(
					"Broadways",
					"Ancient Bread",
					"91002384665",
					236.00D,
					650L,
					60L,
					900L,
					false
			);
			Product prd3 = new Product(
					"Lenovo",
					"SuperComputer",
					"377283927194",
					32.00D,
					500L,
					30L,
					600L,
					false
			);
			Product prd4 = new Product(
					"Socks",
					"Bata",
					"38291029338847",
					236.00D,
					650L,
					60L,
					900L,
					false
			);
			Product prd5 = new Product(
					"Shoes",
					"Sneakers",
					"392934726",
					69.00D,
					500L,
					30L,
					600L,
					false
			);
			Product prd6 = new Product(
					"Trousers",
					"Chinoz Jeans",
					"4839283921128",
					233.00D,
					650L,
					60L,
					900L,
					false
			);
			productRepository.saveAll(
					List.of(prd1, prd2, prd3, prd4, prd5, prd6)
			);
			//Sample Orders
//			Product prdOne = productRepository.findById(1L).orElseThrow(() -> new IllegalStateException("Product with ID: 1 does not exist"));
//			System.out.println("Found Product One: " + prdOne.getName());
//			Product prdTwo = productRepository.findById(2L).orElseThrow(() -> new IllegalStateException("Product with ID: 1 does not exist"));
//			System.out.println("Found Product Two: " + prdTwo.getName());
			Order ord1 = new Order(
					prd1,
					500L,
					false,
					LocalDate.of(2000, Month.JANUARY, 5),
					LocalDate.of(2001, Month.DECEMBER, 15),
					false
			);
			Order ord2 = new Order(
					prd2,
					670L,
					true,
					LocalDate.of(2010, Month.JULY, 16),
					LocalDate.of(2012, Month.NOVEMBER, 25),
					false
			);
			Order ord3 = new Order(
					prd3,
					670L,
					false,
					LocalDate.of(2016, Month.MAY, 07),
					LocalDate.of(2018, Month.AUGUST, 18),
					false
			);
			orderRepository.saveAll(
					List.of(ord1, ord2, ord3)
			);
			//Sample sales
			Sale sal1 = new Sale(
					prd1,
					5L,
					5000.00D,
					LocalDate.of(2001, Month.DECEMBER, 15),
					false
			);
			Sale sal2 = new Sale(
					prd2,
					2L,
					650.00D,
					LocalDate.of(2012, Month.NOVEMBER, 25),
					false
			);
			saleRepository.saveAll(
					List.of(sal1, sal2)
			);
			//Sample roles
//			USER_ROLE,
//		    ADMIN_ROLE,
//		    MANAGER_ROLE,
//		    SUPERUSER_ROLE,
//		    MAINTENANCE_ROLE,
//		    AUDITOR_ROLE,
//		    WAREHOUSE_ATTENDANT_ROLE,
//		    RETAIL_ATTENDANT_ROLE
			AppUserRole aur1 = new AppUserRole(
					"USER_ROLE"
			);
			AppUserRole aur2 = new AppUserRole(
					"ADMIN_ROLE"
			);
			AppUserRole aur3 = new AppUserRole(
					"MANAGER_ROLE"
			);
			AppUserRole aur4 = new AppUserRole(
					"SUPERUSER_ROLE"
			);
			AppUserRole aur5 = new AppUserRole(
					"MAINTENANCE_ROLE"
			);
			AppUserRole aur6 = new AppUserRole(
					"AUDITOR_ROLE"
			);
			AppUserRole aur7 = new AppUserRole(
					"WAREHOUSE_ATTENDANT_ROLE"
			);
			AppUserRole aur8 = new AppUserRole(
					"RETAIL_ATTENDANT_ROLE"
			);
			appUserRoleRepository.saveAll(
					List.of(aur1, aur2, aur3, aur4, aur5, aur6, aur7, aur8)
			);
			//Sample users
			ArrayList<AppUserRole> role1 = new ArrayList<>();
			role1.add(aur8);
			ArrayList<AppUserRole> role2 = new ArrayList<>();
			role2.add(aur7);
			ArrayList<AppUserRole> role3 = new ArrayList<>();
			role3.add(aur4);
			role3.add(aur7);
			role3.add(aur8);
			AppUser au1 = new AppUser(
					"john",
					"doe",
					"john.doe@yahoo.com",
					"123456",
					role1,
					false,
					true
			);
			AppUser au2 = new AppUser(
					"peter",
					"mash",
					"peter.mash@gmail.com",
					"123456",
					role2,
					false,
					true
			);
			AppUser au3 = new AppUser(
					"Elon",
					"MASKER",
					"elon.masker@mailinator.com",
					"123456",
					role3,
					false,
					true
			);
			userService.signUpUser(au1);
			userService.signUpUser(au2);
			userService.signUpUser(au3);
		};
	}
	
}
