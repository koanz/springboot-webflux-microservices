package com.idea.springboot.webflux.app.services;

import com.idea.springboot.webflux.app.models.dtos.ProductDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private WebClient webClient;

    @Override
    public Flux<ProductDTO> getAll() {
        return webClient.get()
                .uri("/")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(ProductDTO.class)
                .onErrorResume(e -> {
                    // Manejo de errores
                    System.err.println("Error al obtener el listado de productos: " + e.getMessage());
                    return Flux.empty(); // Devuelve un Flux vacío en caso de error
                });
    }

    @Override
    public Mono<ProductDTO> findById() {
        return null;
    }

    @Override
    public Mono<ProductDTO> save(ProductDTO request) {
        return null;
    }

    @Override
    public Mono<ProductDTO> update(String id, ProductDTO request) {
        return null;
    }

    @Override
    public Mono<Void> delete(String id) {
        return null;
    }
}
