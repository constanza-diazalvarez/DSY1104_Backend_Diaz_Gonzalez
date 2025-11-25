package com.milsabores;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@RestController
public class ProductosApp {

    public static void main(String[] args) {
        SpringApplication.run(ProductosApp.class, args);
    }

    @GetMapping("/productos")
    public String probar() {
        return "productos 8082";
    }
}