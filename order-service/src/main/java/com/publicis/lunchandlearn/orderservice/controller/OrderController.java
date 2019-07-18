package com.publicis.lunchandlearn.orderservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.cloud.sleuth.annotation.SpanTag;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.publicis.lunchandlearn.orderservice.model.Customer;
import com.publicis.lunchandlearn.orderservice.model.Orders;
import com.publicis.lunchandlearn.orderservice.repository.OrderRepository;
import com.thedeanda.lorem.Lorem;

import brave.Span;
import brave.Tracer;

import lombok.extern.slf4j.Slf4j;

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

  @Autowired
  Tracer tracer;

  @PostMapping
  @NewSpan("add-order-web")
  public Orders add(@RequestBody Orders order) {
    Span span = tracer.currentSpan();
    span.tag("customerId",order.getCustomerId().toString());
    span.tag("productId",order.getProductId().toString());
    span.tag("quantity",order.getQuantity().toString());
    span.tag("cost",order.getTotalCost().toString());
    span.start();
    //call customer
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    ResponseEntity<Customer> customer  = restTemplate.exchange("http://localhost:8080/customers/{id}", HttpMethod.GET,new HttpEntity<>(headers),Customer.class,order.getCustomerId());
    Orders addedOrder =  repository.save(order);

    span.finish();
    log.info("Order added - id : {}, Product Id : {}", addedOrder.getId(), addedOrder.getProductId());
    return addedOrder;
  }

  @PutMapping
  public Orders update(@RequestBody Orders product) {
    return repository.save(product);
  }

  @GetMapping("/{id}")
  @NewSpan("get-order-web")
  public Orders findById(@PathVariable("id")
                        @SpanTag("Order-id") Integer id) {
    log.info("Get Customer : {} ",id);
    return repository.findById(id).get();
  }

}
