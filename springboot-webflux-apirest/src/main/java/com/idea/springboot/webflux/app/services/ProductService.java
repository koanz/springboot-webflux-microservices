package com.idea.springboot.webflux.app.services;

import com.idea.springboot.webflux.app.models.dtos.ProductDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductService {
    public Flux<ProductDTO> getAll();
    public Mono<ProductDTO> findById(String id);

    public Mono<ProductDTO> save(ProductDTO request);

    public Mono<ProductDTO> update(String id, ProductDTO request);

    public Mono<Void> delete(String id);

    public Mono<ProductDTO> findByName(String name);
}
