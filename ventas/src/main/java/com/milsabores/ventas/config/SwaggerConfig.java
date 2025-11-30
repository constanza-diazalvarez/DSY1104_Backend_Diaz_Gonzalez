package com.milsabores.ventas.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuración de Swagger/OpenAPI para documentación de la API
 */
@Configuration
public class SwaggerConfig {

    @Value("${server.port:8083}")
    private String serverPort;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Mil Sabores - API de Ventas/Órdenes")
                        .version("1.0.0")
                        .description("""
                                API REST para la gestión de órdenes de compra de la pastelería Mil Sabores.
                                
                                ## Funcionalidades principales:
                                - Crear nuevas órdenes desde el checkout
                                - Consultar órdenes por ID, número de orden, estado o cliente
                                - Actualizar estado de órdenes (pago, preparación, entrega)
                                - Cancelar órdenes pendientes
                                - Obtener estadísticas de ventas
                                
                                ## Estados de una orden:
                                - **PENDING**: Orden creada, pendiente de pago
                                - **APPROVED**: Pago aprobado
                                - **REJECTED**: Pago rechazado
                                - **CANCELLED**: Orden cancelada
                                - **PROCESSING**: En preparación
                                - **READY**: Lista para entrega/retiro
                                - **DELIVERED**: Entregada
                                """)
                        .contact(new Contact()
                                .name("Equipo Mil Sabores")
                                .email("soporte@milsabores.cl")
                                .url("https://milsabores.cl"))
                        .license(new License()
                                .name("Uso interno")
                                .url("https://milsabores.cl/licencia")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:" + serverPort)
                                .description("Servidor de desarrollo local")
                ));
    }
}
