package com.milsabores.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

//respues con token jwt al hacer login
@Data
@AllArgsConstructor
public class LoginResponseDTO {
    private String token;
    private boolean flag50;
    private boolean flag10;
    private boolean flagCumple;
}
