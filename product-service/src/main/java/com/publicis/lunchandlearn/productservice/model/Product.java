package com.publicis.lunchandlearn.productservice.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer productId;
    String productName;
    Integer quantity;

    public Product(String productName, Integer quantity) {
        this.productName = productName;
        this.quantity = quantity;
    }

    public Product() {
    }
}
