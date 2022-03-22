package io.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class RealworldApplication {

  public static void main(String[] args) {
    SpringApplication.run(RealworldApplication.class, args);
  }
}
