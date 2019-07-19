package com.publicis.lunchandlearn.orderservice.model;

import lombok.Data;

@Data
public class Product {
  Integer productId;
  String productName;
  Integer quantity;
}
