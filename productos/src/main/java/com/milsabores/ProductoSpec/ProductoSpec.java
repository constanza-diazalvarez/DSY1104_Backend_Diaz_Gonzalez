package com.milsabores.ProductoSpec;

import com.milsabores.model.Producto;
import org.springframework.data.jpa.domain.Specification;

public final class ProductoSpec {

    private ProductoSpec() { }

    public static Specification<Producto> categoria(String categoriaId) {
        return (root, query, cb) -> {
            if (categoriaId == null || categoriaId.isBlank()) return null;
            return cb.equal(root.get("categoriaId"), categoriaId);
        };
    }

    public static Specification<Producto> forma(String forma) {
        return (root, query, cb) -> {
            if (forma == null || forma.isBlank()) return null;
            return cb.equal(root.get("tipoForma"), forma);
        };
    }

    public static Specification<Producto> sabor(String sabor) {
        return (root, query, cb) -> {
            if (sabor == null || sabor.isBlank()) return null;
            return cb.isMember(sabor, root.get("sabor"));
        };
    }

    public static Specification<Producto> etiqueta(String etiqueta) {
        return (root, query, cb) -> {
            if (etiqueta == null || etiqueta.isBlank()) return null;
            return cb.isMember(etiqueta, root.get("etiquetas"));
        };
    }

    public static Specification<Producto> tamano(String tamano) {
        return (root, query, cb) -> {
            if (tamano == null || tamano.isBlank()) return null;

            String buscado;
            switch (tamano.toLowerCase()) {
                case "unidad":
                    buscado = "unidad";
                    break;
                case "pequeña":
                    buscado = "8 porciones";
                    break;
                case "mediana":
                    buscado = "12 porciones";
                    break;
                case "grande":
                    buscado = "20 porciones";
                    break;
                default:
                    buscado = tamano;
            }

            return cb.isMember(buscado, root.get("tamanosDisponibles"));
        };
    }
}
