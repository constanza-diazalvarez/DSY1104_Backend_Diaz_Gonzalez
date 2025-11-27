package com.milsabores.dto;

import lombok.Data;

@Data
public class RegistroUsuarioDTO {
    private String nombre;
    private String apellidos;
    private String email;
    private String fechaNacimiento;
    private String password;
    private String codigoPromo;
}
