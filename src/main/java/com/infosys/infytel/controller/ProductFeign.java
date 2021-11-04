package com.infosys.infytel.controller;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


import com.infosys.infytel.dto.productDTO;
@FeignClient("ProductMS")
@CrossOrigin
public interface ProductFeign {
	@GetMapping(value="/product/{prodId}")
	productDTO getProduct(@PathVariable("prodId") String prodId);
	
	
}
