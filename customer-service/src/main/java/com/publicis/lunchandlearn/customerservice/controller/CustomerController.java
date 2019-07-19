package com.publicis.lunchandlearn.customerservice.controller;

import antlr.collections.impl.IntRange;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.cloud.sleuth.annotation.SpanTag;
import org.springframework.http.HttpStatus;
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

import com.publicis.lunchandlearn.customerservice.model.Customer;
import com.publicis.lunchandlearn.customerservice.repository.CustomerRepository;
import com.thedeanda.lorem.Lorem;

@RestController
@RequestMapping("customers")
@Slf4j
public class CustomerController implements ApplicationRunner {

  @Autowired
  CustomerRepository repository;

  @Autowired
  RestTemplate restTemplate;

  @Autowired
  Lorem lorem;

  @PostMapping
  @NewSpan("add-customer-web")
  public Customer add(@RequestBody @SpanTag(value = "customer-name",expression = "Name") Customer customer) {
    Customer addedCustomer =  repository.save(customer);
    log.info("Customer added - id : {}, name : {}", addedCustomer.getId(), addedCustomer.getName());
    return addedCustomer;
  }

  @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE , consumes = MediaType.APPLICATION_JSON_VALUE)
  @NewSpan("update-customer-funds")
  public Customer update(@PathVariable("id") @SpanTag("customerId") Integer customerId,
      @RequestBody  UpdateCustomerCommand updateCustomerCommand) {
     Customer customer = repository.findById(customerId).orElseThrow(RuntimeException::new);
     if(customer.getAvailableFunds()-updateCustomerCommand.getFunds()<0){
       throw new RuntimeException("Wallet Empty");
     }
     customer.setAvailableFunds(customer.getAvailableFunds()-updateCustomerCommand.getFunds());
     repository.save(customer);
     return customer;
  }

  @GetMapping(value = "/{id}",produces = MediaType.APPLICATION_JSON_VALUE , consumes = MediaType.APPLICATION_JSON_VALUE)
  @NewSpan("get-customer-web")
  public Customer findById(@PathVariable("id") @SpanTag("customer-id") Integer id) {
    log.info("Get Customer : {} ",id);
    return repository.findById(id).get();
  }


  @Override
  public void run(ApplicationArguments args) throws Exception {
    if(args.containsOption("load")) {
      Stream.generate(() -> new Customer(lorem.getName(), 100))
          .limit(10)
          .forEach(customer -> {
            ResponseEntity<Customer> responseEntity = restTemplate
                .postForEntity("http://localhost:8080/customers", customer, Customer.class);
            if (responseEntity.getStatusCode() != HttpStatus.OK) {
              log.error("Error Occurred:", responseEntity.getStatusCode().value());
            }
          });
    }

  }

  @Data
  static class UpdateCustomerCommand {
    int funds;
  }
}
