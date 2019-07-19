package com.publicis.lunchandlearn.orderservice.repository;

import java.util.Optional;

import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.publicis.lunchandlearn.orderservice.model.Orders;

@Repository
public interface OrderRepository extends CrudRepository<Orders, Integer> {

  @Override
  @NewSpan("add-order-repo")
  <S extends Orders> S save(S entity);

  @Override
  <S extends Orders> Iterable<S> saveAll(Iterable<S> entities);

  @Override
  @NewSpan("get-order-repo")
  Optional<Orders> findById(Integer integer);

  @Override
  Iterable<Orders> findAll();

  @Override
  Iterable<Orders> findAllById(Iterable<Integer> integers);

  @Override
  long count();
}
