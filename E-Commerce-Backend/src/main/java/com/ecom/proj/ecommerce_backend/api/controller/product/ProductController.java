package com.ecom.proj.ecommerce_backend.api.controller.product;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecom.proj.ecommerce_backend.model.Product;
import com.ecom.proj.ecommerce_backend.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {


  private ProductService productService;

  public ProductController(ProductService productService) {
    this.productService = productService;
  }

  @GetMapping
  public List<Product> getProducts() {
    return productService.getProducts();
  }

}