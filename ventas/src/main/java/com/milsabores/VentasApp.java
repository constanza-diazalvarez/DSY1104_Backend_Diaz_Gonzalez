package com.milsabores;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Aplicación principal del microservicio de Ventas/Órdenes
 * Puerto: 8083
 * Swagger UI: http://localhost:8083/swagger-ui/index.html
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.milsabores", "com.milsabores.ventas"})
public class VentasApp {

    public static void main(String[] args) {
        SpringApplication.run(VentasApp.class, args);
    }
}