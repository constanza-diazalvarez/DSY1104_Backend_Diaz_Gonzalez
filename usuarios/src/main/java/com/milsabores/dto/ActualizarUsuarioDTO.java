package com.milsabores.dto;

import lombok.Data;

@Data
public class ActualizarUsuarioDTO {
    private String nombre;
    private String apellidos;
    private String fechaNacimiento;
    private String password;
    private String codigoPromo;
}
