package com.infosys.infytel.controller;


import java.util.List; 

import org.slf4j.Logger; 
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.infosys.infytel.dto.OrderDTO;
import com.infosys.infytel.dto.orderstatus;

import com.infosys.infytel.service.OrderService;


@RestController
@CrossOrigin
@RequestMapping(value="/api")
public class OrderController {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	OrderService oService;

	@Autowired
	private Environment environment;
	
	
	@GetMapping(value = "/orders/{orderId}",  produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<OrderDTO> findAllOrdersByOrderId(@PathVariable String orderId) throws Exception{
		try {
			OrderDTO cust= oService.viewOrder(orderId);
			return new ResponseEntity<>(cust,HttpStatus.OK);
		} catch (Exception exception) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, environment.getProperty(exception.getMessage()), exception);
		}
	}
	@PostMapping(value = "/orders/placeOrder/{buyerId}",  consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> placeOrder(@PathVariable String buyerId, @RequestBody String Address) throws Exception{
		try {
			String sd=oService.placeOrder(buyerId, Address);
			String successMessage = environment.getProperty("API.ORDER_SUCCESS") + sd ;
			return new ResponseEntity<>(successMessage, HttpStatus.CREATED);
		}catch(Exception exception) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, environment.getProperty(exception.getMessage()), exception);
		}	
	}
	@PostMapping(value = "/orders/reOrder/{orderId}",  consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> reOrder(@PathVariable String orderId, @RequestBody Integer quantity) throws Exception{
		try {
			String sd=oService.reOrder(orderId,quantity);
			String successMessage = environment.getProperty("API.ORDER_SUCCESS") + sd ;
			return new ResponseEntity<>(successMessage, HttpStatus.CREATED);
		}catch(Exception exception) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, environment.getProperty(exception.getMessage()), exception);
		}	
	}
	@GetMapping(value = "/orders/{buyerId}",  produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<OrderDTO>> findproductbyBuyerId(@PathVariable String buyerId) throws Exception{
		try {
			List<OrderDTO> cust= oService.viewOrdersByBuyer(buyerId);
			return new ResponseEntity<>(cust,HttpStatus.OK);
		} catch (Exception exception) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, environment.getProperty(exception.getMessage()), exception);
		}
	}
	@PutMapping(value = "/orders/update/{orderId}",  produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> updatestatus(@RequestBody orderstatus status,@PathVariable String orderId ) throws Exception{
		try {
		    oService.updateStatus(status,orderId);
			String successMessage = environment.getProperty("API.UPDATE_SUCCESS") ;
			return new ResponseEntity<>(successMessage, HttpStatus.OK);
		} catch (Exception exception) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, environment.getProperty(exception.getMessage()), exception);
	
		}
	}
}
