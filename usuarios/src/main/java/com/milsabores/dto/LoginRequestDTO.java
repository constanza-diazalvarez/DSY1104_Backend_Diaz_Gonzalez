package com.milsabores.dto;

import lombok.Data;

//cuando el usuario inicia sesion
@Data
public class LoginRequestDTO {
    private String email;
    private String password;
}
