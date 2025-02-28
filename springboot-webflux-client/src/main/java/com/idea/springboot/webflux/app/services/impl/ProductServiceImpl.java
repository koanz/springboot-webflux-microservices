package com.idea.springboot.webflux.app.services;

import com.idea.springboot.webflux.app.exceptions.ProductNotFoundException;
import com.idea.springboot.webflux.app.models.dtos.ProductDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductServiceImpl implements ProductService {
    private Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Autowired
    private WebClient webClient;

    @Override
    public Flux<ProductDTO> getAll() {
        logger.info("Retrieve Flux of ProductDTO via WebClient");

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
    public Mono<ProductDTO> findById(String id) {
        logger.info("Retrieve by id Mono of ProductDTO via WebClient");

        return webClient.get()
                .uri("/{id}", id)  // Asegúrate de que la variable {id} se expanda correctamente aquí
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(ProductDTO.class)
                .onErrorResume(e -> {
                    logger.error("Error Product Not Found: " + e.getMessage());
                    return Mono.error(new ProductNotFoundException(id));
                });
    }

    @Override
    public Mono<ProductDTO> save(ProductDTO request) {
        return webClient.post()
                .uri("/create")
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ProductDTO.class);
    }

    @Override
    public Mono<ProductDTO> update(String id, ProductDTO request) {
        return webClient.put()
                .uri("/update/{id}")
                .header("id", id)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ProductDTO.class);
    }

    @Override
    public Mono<Void> delete(String id) {
        /*return webClient.delete()
                .uri("/delete/{id}")
                .header("id", id)
                .accept(MediaType.APPLICATION_JSON)
                .e;*/
        return null;
    }
}
