package com.publicis.lunchandlearn.productservice.repository;

import java.util.Optional;

import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.publicis.lunchandlearn.productservice.model.Product;

@Repository
public interface ProductRepository  extends CrudRepository<Product,Integer> {

  @Override
  @NewSpan("add-product-repo")
  <S extends Product> S save(S entity);

  @Override
  <S extends Product> Iterable<S> saveAll(Iterable<S> entities);

  @Override
  @NewSpan("get-product-repo")
  Optional<Product> findById(Integer integer);

  @Override
  Iterable<Product> findAll();

  @Override
  Iterable<Product> findAllById(Iterable<Integer> integers);

  @Override
  long count();
}
