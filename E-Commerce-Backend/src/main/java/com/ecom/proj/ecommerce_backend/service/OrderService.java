package com.ecom.proj.ecommerce_backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ecom.proj.ecommerce_backend.model.LocalUser;
import com.ecom.proj.ecommerce_backend.model.WebOrder;
import com.ecom.proj.ecommerce_backend.model.dao.WebOrderDAO;

@Service
public class OrderService {

	private WebOrderDAO webOrderDAO;

	public OrderService(WebOrderDAO webOrderDAO) {
		this.webOrderDAO = webOrderDAO;
	}
	
	public List<WebOrder> getOrders(LocalUser user){
		
		return webOrderDAO.findByUser(user);
		
	}
	
	
}
