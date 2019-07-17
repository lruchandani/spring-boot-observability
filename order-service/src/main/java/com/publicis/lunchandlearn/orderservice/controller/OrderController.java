package com.publicis.lunchandlearn.orderservice.controller;

import lombok.extern.slf4j.Slf4j;

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

import com.publicis.lunchandlearn.orderservice.model.Order;
import com.publicis.lunchandlearn.orderservice.repository.OrderRepository;
import com.thedeanda.lorem.Lorem;

@RestController
@RequestMapping("orders")
@Slf4j
public class OrderController  {

  @Autowired
  OrderRepository repository;

  @Autowired
  RestTemplate restTemplate;

  @Autowired
  Lorem lorem;

  @PostMapping
  @NewSpan("add-product-web")
  public Order add(@RequestBody
  @SpanTag(value = "product-name",expression = "#{product.name}")  Order order) {
    Order addedOrder =  repository.save(order);
    log.info("Customer added - id : {}, name : {}", addedOrder.getOrderId(), addedOrder.getProductId());
    return addedOrder;
  }

  @PutMapping
  public Order update(@RequestBody Order product) {
    return repository.save(product);
  }

  @GetMapping("/{id}")
  @NewSpan("get-product-web")
  public Order findById(@PathVariable("id")
                        @SpanTag("product-id") Integer id) {
    log.info("Get Customer : {} ",id);
    return repository.findById(id).get();
  }

}
