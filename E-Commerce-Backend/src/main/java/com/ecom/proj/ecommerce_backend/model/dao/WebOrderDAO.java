package com.ecom.proj.ecommerce_backend.model.dao;

import java.util.List;

import org.springframework.data.repository.ListCrudRepository;

import com.ecom.proj.ecommerce_backend.model.LocalUser;
import com.ecom.proj.ecommerce_backend.model.WebOrder;

public interface WebOrderDAO extends ListCrudRepository<WebOrder, Long>{

	  List<WebOrder> findByUser(LocalUser user);
	
}
