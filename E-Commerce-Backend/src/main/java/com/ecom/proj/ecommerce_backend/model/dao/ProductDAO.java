package com.ecom.proj.ecommerce_backend.model.dao;

import org.springframework.data.repository.ListCrudRepository;

import com.ecom.proj.ecommerce_backend.model.Product;

public interface ProductDAO extends ListCrudRepository<Product, Long>{

	
	
}
