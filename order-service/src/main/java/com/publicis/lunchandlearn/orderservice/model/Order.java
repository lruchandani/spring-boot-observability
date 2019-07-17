package com.publicis.lunchandlearn.orderservice.model;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Integer orderId;
  Integer customerId;
  Integer productId;
  Integer quantity;
  Double  totalCost;
}
