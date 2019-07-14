package com.publicis.lunchandlearn.customerservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.publicis.lunchandlearn.customerservice.model.Customer;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Integer> {

  @Override
  @NewSpan("add-customer-repo")
  <S extends Customer> S save(S entity);

  @Override
  @NewSpan("get-customer-repo")
  Optional<Customer> findById(Integer integer);

  @Override
  boolean existsById(Integer integer);

  @Override
  Iterable<Customer> findAll();

  @Override
  Iterable<Customer> findAllById(Iterable<Integer> integers);

  List<Customer> findByName(String name);

}
