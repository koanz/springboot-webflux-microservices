package com.idea.springboot.webflux.app.repositories;

import com.idea.springboot.webflux.app.models.documents.Category;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface CategoryRepository extends ReactiveMongoRepository<Category, String> {
    public Mono<Category> findByName(String name);
}
