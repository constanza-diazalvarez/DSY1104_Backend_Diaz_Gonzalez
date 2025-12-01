package com.milsabores;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class UsuariosApp {

    public static void main(String[] args) {
        SpringApplication.run(UsuariosApp.class, args);
    }

    @GetMapping("/usuarios")
    public String probar() {
        return "usuarios 8081";
    }
}
