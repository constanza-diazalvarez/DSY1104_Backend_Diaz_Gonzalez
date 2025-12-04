package com.milsabores.ventas.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

/**
 * DTO para la información del cliente en una orden
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Información del cliente para la orden")
public class CustomerInfoDTO {

    @NotBlank(message = "El nombre es requerido")
    @Size(max = 150, message = "El nombre no puede exceder 150 caracteres")
    @Schema(description = "Nombre completo del cliente", example = "Juan Pérez González")
    private String nombre;

    @NotBlank(message = "El email es requerido")
    @Email(message = "El formato del email es inválido")
    @Size(max = 100, message = "El email no puede exceder 100 caracteres")
    @Schema(description = "Email del cliente", example = "juan.perez@email.com")
    private String email;

    @NotBlank(message = "El teléfono es requerido")
    @Pattern(regexp = "^\\+?[\\d\\s-]{8,20}$", message = "El formato del teléfono es inválido")
    @Schema(description = "Teléfono de contacto", example = "+56 9 1234 5678")
    private String telefono;

    @NotBlank(message = "La dirección es requerida")
    @Size(max = 255, message = "La dirección no puede exceder 255 caracteres")
    @Schema(description = "Dirección de entrega", example = "Av. Providencia 1234, Depto 501")
    private String direccion;

    @NotBlank(message = "La comuna es requerida")
    @Size(max = 100, message = "La comuna no puede exceder 100 caracteres")
    @Schema(description = "Comuna de entrega", example = "Providencia")
    private String comuna;

    @NotBlank(message = "La ciudad es requerida")
    @Size(max = 100, message = "La ciudad no puede exceder 100 caracteres")
    @Schema(description = "Ciudad de entrega", example = "Santiago")
    private String ciudad;
}
