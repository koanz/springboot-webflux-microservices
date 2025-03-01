package com.idea.springboot.webflux.app.services.impl;

import com.idea.springboot.webflux.app.exceptions.CategoryNotFountByIdException;
import com.idea.springboot.webflux.app.exceptions.CustomNotFoundException;
import com.idea.springboot.webflux.app.exceptions.ProductNotFoundException;
import com.idea.springboot.webflux.app.models.dtos.ProductDTO;
import com.idea.springboot.webflux.app.models.responses.MessageResponse;
import com.idea.springboot.webflux.app.services.ProductService;
import org.bson.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
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
                    logger.error("Error Flux<ProductDTO> No records Found: " + e.getMessage());
                    return Mono.error(new CustomNotFoundException("No records exist"));
                });
    }

    @Override
    public Mono<ProductDTO> findById(String id) {
        logger.info("Retrieve Mono<ProductDTO> by id via WebClient");

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
        logger.info("Create Product via WebClient");

        return webClient.post()
                .uri("/create")
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ProductDTO.class);
    }

    @Override
    public Mono<ProductDTO> update(String id, ProductDTO request) {
        logger.info("Update Product via WebClient");

        return webClient.put()
                .uri("/update/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response ->
                        response.bodyToMono(String.class)
                                .flatMap(errorBody -> {
                                    // Buscar una alternativa mejor para capturar el error
                                    JsonObject errorJson = new JsonObject(errorBody);
                                    String errorMessage = errorJson.getJson();
                                    logger.error("Exception error in service client of Product: " + errorMessage);

                                    if (errorMessage.contains("Category with id")) {
                                        return Mono.error(new CategoryNotFountByIdException(request.getCategory().getId()));
                                    }

                                    if (errorMessage.contains("Product with id")) {
                                        return Mono.error(new ProductNotFoundException(id));
                                    }

                                    return Mono.error(new CustomNotFoundException("Unexpected error 400 has occur in service client of Product (update product): " + errorMessage));
                                })
                )
                .bodyToMono(ProductDTO.class)
                .onErrorResume(e -> {
                    // Manejo de errores
                    logger.error("Unexpected error has occur in service client of Product (update product): " + e.getMessage());

                    return Mono.error(new CustomNotFoundException(e.getMessage()));
                });
    }

    @Override
    public Mono<MessageResponse> delete(String id) {
        logger.info("Delete Product by id (WebClient): " + id);

        return webClient.delete()
                .uri("/delete/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> response.bodyToMono(String.class)
                        .flatMap(errorBody -> {
                            // Buscar una alternativa mejor para capturar el error
                            JsonObject errorJson = new JsonObject(errorBody);
                            String errorMessage = errorJson.getJson();
                            logger.error("Exception error in service client of Product: " + errorMessage);

                            if (errorMessage.contains("Product with id")) {
                                return Mono.error(new ProductNotFoundException(id));
                            }

                            return Mono.error(new CustomNotFoundException("Unexpected error 400 has occur in service client of Product (update product): " + errorMessage));
                        })
                )
                .bodyToMono(MessageResponse.class)
                .onErrorResume(e -> {
                    logger.error("Unexpected error has occurred in service client of Product (delete product): " + e.getMessage());
                    return Mono.error(new CustomNotFoundException(e.getMessage()));
                });

    }
}
