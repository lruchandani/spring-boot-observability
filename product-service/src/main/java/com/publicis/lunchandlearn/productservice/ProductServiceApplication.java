package com.publicis.lunchandlearn.productservice;

import io.micrometer.core.instrument.MeterRegistry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import com.thedeanda.lorem.Lorem;
import com.thedeanda.lorem.LoremIpsum;

@SpringBootApplication
public class ProductServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(ProductServiceApplication.class, args);
  }

  @Bean
  public Lorem getLorem(){
    return LoremIpsum.getInstance();
  }

  @Bean
  public RestTemplate restTemplate(){
    return new RestTemplate();
  }

  @Bean
  MeterRegistryCustomizer<MeterRegistry> metricsCommonTags() {
    return registry -> registry.config().commonTags("application", "product-service");
  }
}
