package com.publicis.lunchandlearn.orderservice.repository;

import java.util.Optional;

import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.publicis.lunchandlearn.orderservice.model.Order;

@Repository
public interface OrderRepository extends CrudRepository<Order, Integer> {

  @Override
  @NewSpan("add-order-repo")
  <S extends Order> S save(S entity);

  @Override
  <S extends Order> Iterable<S> saveAll(Iterable<S> entities);

  @Override
  @NewSpan("get-order-repo")
  Optional<Order> findById(Integer integer);

  @Override
  Iterable<Order> findAll();

  @Override
  Iterable<Order> findAllById(Iterable<Integer> integers);

  @Override
  long count();
}
