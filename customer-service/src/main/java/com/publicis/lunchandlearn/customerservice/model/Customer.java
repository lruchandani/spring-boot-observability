package com.publicis.lunchandlearn.customerservice.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
public class Customer {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  private String name;
  private int availableFunds;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getAvailableFunds() {
    return availableFunds;
  }

  public void setAvailableFunds(int availableFunds) {
    this.availableFunds = availableFunds;
  }

  public Customer() {
  }

  public Customer(String name, int availableFunds) {
    this.name = name;
    this.availableFunds = availableFunds;
  }
}
