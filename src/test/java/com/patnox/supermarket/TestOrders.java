package com.patnox.supermarket;

import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.Month;

//import org.junit.Before;
//import org.junit.Test;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
//import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.boot.test.mock.mockito.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.patnox.supermarket.orders.*;
import com.patnox.supermarket.orders.Order;
import com.patnox.supermarket.products.Product;

import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.test.context.SpringBootTest.*;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.context.*;
import org.springframework.web.reactive.function.client.*;

import java.net.*;

@AutoConfigureMockMvc
//@WebMvcTest
//@RunWith(SpringRunner.class)
//@WebMvcTest(value = OrderController.class)
//// @WebAppConfiguration
//@SpringBootConfiguration(classes = SupermarketApplication.class)
@WithMockUser
@ComponentScan(basePackages = "patnox.supermarket")
//@Import({OrderController.class, AopAutoConfiguration.class, WebClientTestConfiguration.class})
//@RunWith(SpringRunner.class)
//@WebMvcTest(controllers = OrderController.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
//public class TestOrders extends SupermarketApplicationTests 
@Slf4j
//@EnableAspectJAutoProxy
//@ImportAutoConfiguration
//@Import(AopAutoConfiguration.class)
@TestPropertySource(locations = "classpath:test.properties")
//@ContextConfiguration(classes = OrderController.class)
@ContextConfiguration(classes = {SupermarketApplication.class, OrderController.class, OrderService.class, Order.class, OrderRepository.class})
public class TestOrders
{
	@Autowired
	private WebApplicationContext webApplicationContext;
	
	// bind the above RANDOM_PORT
    @LocalServerPort
    private int port;
    
    String serverURL = "";
//    private String serverURL = "test";
//    
//    try
//    {
////	    URL testURL = new URL("http://localhost:" + port + "/");
////	    serverURL = testURL.toString();
//    }
//    catch(Exception e) {}
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
    private TestRestTemplate restTemplate;
	
	@MockBean
	private OrderService orderService;
	
	@MockBean
	private OrderController orderController;
	
	Product prd1 = new Product(
			"ButterToast",
			"Milk Bread",
			"37893664584",
			732.00D,
			500L,
			30L,
			600L,
			false
	);
	
	Order ord1 = new Order(
			prd1,
			500L,
			false,
			LocalDate.of(2000, Month.JANUARY, 5),
			LocalDate.of(2001, Month.DECEMBER, 15),
			false
	);
	
	String newOrder = "{\n"
			+ "    \"product_id\":\"3\",\n"
			+ "    \"quantity\":\"4361\",\n"
			+ "    \"is_fullfilled\":\"true\",\n"
			+ "    \"date_ordered\":\"2019-07-06\",\n"
			+ "    \"date_fullfilled\":\"2021-01-11\"\n"
			+ "}";

//	@Before
//	public void setup() {
//		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
//	}

	@Test
	public void testAddingOrder() throws Exception {
//		mockMvc.perform(get("/login")).andExpect(status().isOk())
//				.andExpect(content().contentType("application/json;charset=UTF-8"))
//				.andExpect(jsonPath("$.name").value("emp1")).andExpect(jsonPath("$.designation").value("manager"))
//				.andExpect(jsonPath("$.empId").value("1")).andExpect(jsonPath("$.salary").value(3000));
		// Mockito.when(studentService.retrieveCourse(Mockito.anyString(), Mockito.anyString())).thenReturn(mockCourse);
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		try
	    {
		    URL testURL = new URL("http://127.0.0.1:" + port + "/");
		    serverURL = testURL.toString();
	    }
	    catch(Exception e) {}
		
		log.info("Got server url as: {}", serverURL);
		
		Mockito.when(orderService.getOrder(Mockito.anyLong())).thenReturn(ord1);
//		RequestBuilder requestBuilder = MockMvcRequestBuilders
//				.post("/order/")
//				.accept(MediaType.APPLICATION_JSON).content(newOrder)
//				.contentType(MediaType.APPLICATION_JSON);
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post("/api/v1/order")
				.content(newOrder)
				.contentType(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();
		log.info("Got post 1 response as: {}", response.getStatus());
		
//		ResponseEntity<String> responser = restTemplate.postForEntity(serverURL + "api/v1/order", newOrder, null);
//		log.info("Got post 2 response as: {}", responser);

		//assertEquals(HttpStatus.CREATED.value(), response.getStatus());
		//assertEquals(HttpStatus.CREATED.value(), responser.getStatusCodeValue());
		assertEquals(HttpStatus.OK.value(), response.getStatus());
		//assertEquals(HttpStatus.OK.value(), responser.getStatusCodeValue());
	}
	
	@TestConfiguration
    public static class WebClientTestConfiguration 
    {
        @Bean
        public WebClient getWebClient(final WebClient.Builder builder) 
        {
            WebClient webClient = builder
                    .baseUrl("http://localhost")
                    .build();
            log.info("WebClient Instance Created During Testing, using static inner class: {}", webClient);
            return webClient;
        }
    }
	
}
