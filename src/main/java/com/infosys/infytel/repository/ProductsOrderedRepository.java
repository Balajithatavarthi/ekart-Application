package com.infosys.infytel.repository;



import org.springframework.data.jpa.repository.JpaRepository; 

import com.infosys.infytel.entity.ProductsOrdered;

public interface ProductsOrderedRepository extends JpaRepository<ProductsOrdered, String> {
	


}
