package com.publicis.lunchandlearn.orderservice.model;

import lombok.Data;

@Data
public class Customer {
  private Integer id;
  private String name;
  private int availableFunds;
}
