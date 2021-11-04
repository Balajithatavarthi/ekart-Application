package com.infosys.infytel.controller;


import java.util.List; 

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.infosys.infytel.dto.BuyerDTO;
import com.infosys.infytel.dto.CartDTO;
@FeignClient("UserMS")
@CrossOrigin
public interface UserFeign{
	
	@GetMapping(value="api/buyer/{buyerId}/Cart")
	List<CartDTO> getCartDetails(@PathVariable("buyerId") String buyerId);
	
	@DeleteMapping(value="api/buyer/{buyerId}/cart")
	String deleteAllCartItem(@PathVariable("buyerId") String buyerId);
	
	@GetMapping(value="api/buyer/{buyerId}")
	BuyerDTO findBuyer(@PathVariable("buyerId") String buyerId);
}
