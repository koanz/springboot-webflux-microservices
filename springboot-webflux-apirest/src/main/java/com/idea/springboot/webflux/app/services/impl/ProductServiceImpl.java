package com.idea.springboot.webflux.app.services.impl;

import com.idea.springboot.webflux.app.exceptions.CategoryNotFountByIdException;
import com.idea.springboot.webflux.app.exceptions.CustomNotFoundException;
import com.idea.springboot.webflux.app.exceptions.ProductNotFoundByNameException;
import com.idea.springboot.webflux.app.exceptions.ProductNotFoundException;
import com.idea.springboot.webflux.app.mappers.ProductMapper;
import com.idea.springboot.webflux.app.models.documents.Product;
import com.idea.springboot.webflux.app.models.dtos.ProductDTO;
import com.idea.springboot.webflux.app.repositories.ProductRepository;
import com.idea.springboot.webflux.app.services.CategoryService;
import com.idea.springboot.webflux.app.services.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.Objects;

@Service
public class ProductServiceImpl implements ProductService {
    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Autowired
    private ProductRepository repository;

    @Autowired
    private ProductMapper mapper;

    @Autowired
    private CategoryService categoryService;

    @Override
    public Flux<ProductDTO> getAll() {
        logger.info("Find all Product");

        return repository.findAll()
                .filter(Objects::nonNull)
                .map(mapper::toDto)
                .switchIfEmpty(Mono.error(new CustomNotFoundException("No records exist")));
    }

    @Override
    public Mono<ProductDTO> findById(String id) {
        logger.info("Find Product by id: " + id);

        return repository.findById(id)
                .map(mapper::toDto)
                .switchIfEmpty(Mono.error(new ProductNotFoundException(id)));
    }

    @Transactional
    @Override
    public Mono<ProductDTO> save(ProductDTO request) {
        logger.info("Creating Product: " + request.getName());

        return categoryService.findById(request.getCategory().getId())
                .flatMap(category -> {
                    Product product = mapper.toEntity(request);
                    product.setCategory(category);
                    product.setCreatedAt(new Date());
                    return repository.save(product);
                })
                .map(mapper::toDto)
                .switchIfEmpty(Mono.error(new CategoryNotFountByIdException(request.getCategory().getId())));
    }

    @Transactional
    @Override
    public Mono<ProductDTO> update(String id, ProductDTO request) {
        logger.info("Updating Product: " + id);

        return repository.findById(id)
                .switchIfEmpty(Mono.error(new ProductNotFoundException(request.getId())))
                .flatMap(existingProduct -> {
                    return categoryService.findById(request.getCategory().getId())
                            .switchIfEmpty(Mono.error(new CategoryNotFountByIdException(request.getCategory().getId())))
                            .flatMap(category -> {
                                existingProduct.setName(request.getName());
                                existingProduct.setPrice(request.getPrice());
                                existingProduct.setCategory(category);
                                return repository.save(existingProduct);
                            });
                })
                .map(mapper::toDto)
                .onErrorResume(e -> {
                    logger.error("Unexpected error while trying to update a Product: ", e);
                    return Mono.error(e);
                });
    }

    @Transactional
    @Override
    public Mono<Void> delete(String id) {
        logger.info("Deleting Product: " + id);

        return repository.findById(id)
                .switchIfEmpty(Mono.error(new ProductNotFoundException(id)))
                .flatMap(product -> repository.delete(product))
                .doOnError(throwable -> logger.error("Error in delete method", throwable));
    }

    @Override
    public Mono<ProductDTO> findByName(String name) {
        return repository.findByName(name)
                .map(mapper::toDto)
                .switchIfEmpty(Mono.error(new ProductNotFoundByNameException(name)));
    }
}
