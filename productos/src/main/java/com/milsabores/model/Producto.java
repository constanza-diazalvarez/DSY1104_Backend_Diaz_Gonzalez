package com.milsabores.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "productos")
public class Producto {

    @Id
    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String nombre;

    private String categoriaId;
    private String tipoForma;

    @ElementCollection
    private List<String> tamanosDisponibles;

    private int precioCLP;
    private int stock;
    private boolean personalizable;
    private int maxMsgChars;

    @Column(length = 500)
    private String descripcion;

    @ElementCollection
    private List<String> etiquetas;

    @ElementCollection
    private List<String> sabor;

    private String imagen;
}
