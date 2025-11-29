package com.milsabores.service;

import com.milsabores.model.Producto;
import com.milsabores.repository.ProductoRepository;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

@Service
public class ProductoService {

    private final ProductoRepository repo;

    public ProductoService(ProductoRepository repo) {
        this.repo = repo;
    }
    public Page<Producto> buscar(Specification<Producto> spec, Pageable pageable) {
        return repo.findAll(spec, pageable);
    }

    @Transactional
    public Producto actualizarStock(String code, int cantidad) {
        Producto p = repo.findById(code)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        if (p.getStock() < cantidad)
            throw new RuntimeException("Stock insuficiente");

        p.setStock(p.getStock() - cantidad);
        return repo.save(p);
    }
}
