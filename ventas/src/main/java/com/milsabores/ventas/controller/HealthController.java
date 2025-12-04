package com.milsabores.ventas.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Controlador para verificar el estado del servicio
 */
@RestController
@RequestMapping("/api/health")
@Tag(name = "Health", description = "Endpoints para verificar el estado del servicio")
public class HealthController {

    @GetMapping
    @Operation(summary = "Verificar estado del servicio", description = "Retorna información sobre el estado del microservicio")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> health = new HashMap<>();
        health.put("service", "ventas");
        health.put("status", "UP");
        health.put("timestamp", LocalDateTime.now());
        health.put("port", 8083);
        health.put("swagger-ui", "http://localhost:8083/swagger-ui.html");
        return ResponseEntity.ok(health);
    }

    @GetMapping("/ping")
    @Operation(summary = "Ping", description = "Endpoint simple para verificar conectividad")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("pong");
    }
}
