package com.milsabores;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// El exclude es para que arranque AHORA sin pedirte contraseña de base de datos
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
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
