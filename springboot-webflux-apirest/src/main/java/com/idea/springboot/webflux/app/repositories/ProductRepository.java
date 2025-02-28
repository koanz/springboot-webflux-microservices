package com.idea.springboot.webflux.app.repositories;

import com.idea.springboot.webflux.app.models.documents.Product;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface ProductRepository extends ReactiveMongoRepository<Product, String> {
    public Mono<Product> findByName(String name);
}
