package com.milsabores.spec;

import com.milsabores.model.Producto;
import org.springframework.data.jpa.domain.Specification;

public final class ProductoSpec {

    private ProductoSpec() { }

    /*retorna una Specification para la entidad producto
    * una specification es un objeto que representa una condicion que se le agregara a una query
    * dice como filtrar cuando la consulta se ejecuta
    * cualquier métod que devuelva un Specification<T> puede combinarse
    * con .and(), .or() y .where(), porque las Specifications están diseñadas
    * para ser filtros componibles
    * ej: where(categoria(X)).and(forma("redonda")).or(etiqueta("vegano"))
    *
    * formato de la interfaz Specification<T>
    * Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb);
    * →cuando se retorna una spec, en realidad se esta devolviendo una funcion que implementa ese metodo
    *   root: representa la entidad principal sobre la que se esta haciendo la consulta (tabla productos)
    *       root.get("categoriaId") → traeme la columna categoriaId de la tabla producto (lo llama usando el nombre del atributo, no de la columna)
    *   query: consulta completa
    *   cb: constuctos de condiciones SQL. Lo que se construye con cb es un Predicate, osea, una condicion
    *   que va dentro del WHERE
    *
    * */
    public static Specification<Producto> categoria(String categoriaId) {
        return (root, query, cb) -> {
            if (categoriaId == null || categoriaId.isBlank()) return null;
            return cb.equal(root.get("categoriaId"), categoriaId);
            /*↑retorna el criterial builder para construir el predicate
            * root.get("categoriaId"): hibernate combierte ese nombre del atributo en el nombre de la columna
            * categoriaId: el valor que se envio (TT, TC, TV, etc)
            * finalmente:
            *   cb.equals(categoria_id, TT)
            *   →construye un: WHERE categoria_id = 'TT'
            *       **equals: comparacion directa
            *         isMember: valor dentro de una coleccion
            * */
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
