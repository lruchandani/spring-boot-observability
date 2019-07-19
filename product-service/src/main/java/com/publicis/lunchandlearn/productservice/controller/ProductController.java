package com.publicis.lunchandlearn.productservice.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.cloud.sleuth.annotation.SpanTag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.publicis.lunchandlearn.productservice.model.Product;
import com.publicis.lunchandlearn.productservice.repository.ProductRepository;
import com.thedeanda.lorem.Lorem;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("products")
@Slf4j
public class ProductController implements ApplicationRunner {

  @Autowired
  ProductRepository repository;

  @Autowired
  RestTemplate restTemplate;

  @Autowired
  Lorem lorem;

  @PostMapping
  @NewSpan("add-product-web")
  public Product add(@RequestBody
      @SpanTag(value = "product-name",expression = "#{product.productName}")  Product product) {
    Product addedProduct =  repository.save(product);
    log.info("Product added - id : {}, name : {}", addedProduct.getProductId(), addedProduct.getProductName());
    return addedProduct;
  }

  @PutMapping("/{id}")
  public Product update(@PathVariable("id") @SpanTag("productId") Integer id,
      @RequestBody UpdateProductCommand productCommand) {
    Product product = repository.findById(id).orElseThrow(()->new RuntimeException("Product Not found"));
    if(product.getQuantity()-productCommand.getQuanity()<0){
      throw new RuntimeException("Product Out of Stock");
    }
    product.setQuantity(product.getQuantity()-productCommand.getQuanity());
    return repository.save(product);
  }

  @GetMapping("/{id}")
  @NewSpan("get-product-web")
  public Product findById(@PathVariable("id") @SpanTag("product-id") Integer id) {
    log.info("Get Product : {} ",id);
    return repository.findById(id).get();
  }


  @Override
  public void run(ApplicationArguments args) throws Exception {
    if(args.containsOption("load")) {
      Map.of("Airbus A380",10, "Dreamliner 737",20, "Airbus A320", 30)
          .forEach((productName,quantity)-> {
            ResponseEntity<Product> responseEntity = restTemplate
                .postForEntity("http://localhost:8082/products", new Product(productName,quantity), Product.class);
            if (responseEntity.getStatusCode() != HttpStatus.OK) {
              log.error("Error Occurred:", responseEntity.getStatusCode().value());
            }
          });
    }

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  static class UpdateProductCommand {
    int quanity;
  }
}
