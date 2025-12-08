package com.milsabores.controller;

import com.milsabores.spec.ProductoSpec;
import com.milsabores.model.Producto;
import com.milsabores.service.ProductoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/productos")
@CrossOrigin(origins = "*")
@Tag(name = "Productos", description = "Gestión del catálogo de productos con filtros y paginación")
public class ProductoController {

    private final ProductoService service;

    public ProductoController(ProductoService service) {
        this.service = service;
    }

    @Operation(
            summary = "Listar productos",
            description = "Permite filtrar productos por categoría, forma, sabor, etiqueta y tamaño. También admite paginación.",
            parameters = {
                    @Parameter(name = "categoria", description = "Categoría del producto. Ej: 'TC', 'TT'"),
                    @Parameter(name = "forma", description = "Forma del producto. Ej: 'circular', 'cuadrada'"),
                    @Parameter(name = "sabor", description = "Sabor del producto. Ej: 'chocolate', 'vainilla'"),
                    @Parameter(name = "etiqueta", description = "Etiqueta especial. Ej: 'sin-azucar', 'vegano'"),
                    @Parameter(name = "tamano", description = "Tamaño del producto. Ej: '8 porciones', '12 porciones'"),
                    @Parameter(name = "page", description = "Número de página (por defecto 0)"),
                    @Parameter(name = "size", description = "Tamaño de la página (por defecto 8)")
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Listado de productos encontrado",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Page.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Parámetros de consulta inválidos"),
                    @ApiResponse(responseCode = "404", description = "No se encontraron productos")
            }
    )
    @GetMapping
    public Page<Producto> listar(
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) String forma,
            @RequestParam(required = false) String sabor,
            @RequestParam(required = false) String etiqueta,
            @RequestParam(required = false) String tamano,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size
    ) {

        categoria = normalizar(categoria);
        forma = normalizar(forma);
        sabor = normalizar(sabor);
        etiqueta = normalizar(etiqueta);
        tamano = normalizar(tamano);

        Pageable pageable = PageRequest.of(page, size);

        Specification<Producto> spec =
                Specification.where(ProductoSpec.categoria(categoria))
                        .and(ProductoSpec.forma(forma))
                        .and(ProductoSpec.sabor(sabor))
                        .and(ProductoSpec.etiqueta(etiqueta))
                        .and(ProductoSpec.tamano(tamano));

        return service.buscar(spec, pageable);
    }

    private String normalizar(String valor) {
        return (valor == null
                || valor.equalsIgnoreCase("undefined")
                || valor.equalsIgnoreCase("null")
                || valor.isBlank())
                ? null
                : valor;
    }
}

