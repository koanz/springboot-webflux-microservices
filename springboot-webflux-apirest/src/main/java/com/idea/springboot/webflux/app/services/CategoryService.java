package com.idea.springboot.webflux.app.services;

import com.idea.springboot.webflux.app.models.documents.Category;
import com.idea.springboot.webflux.app.models.dtos.CategoryDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CategoryService {
    public Mono<CategoryDTO> findDTOById(String id);
    public Mono<Category> findById(String id);
    public Flux<CategoryDTO> getAll();
    public Mono<CategoryDTO> save(CategoryDTO request);
    public Mono<CategoryDTO> update(String id, CategoryDTO request);
    public Mono<Void> delete(String id);
    public Mono<CategoryDTO> findByName(String name);
}
