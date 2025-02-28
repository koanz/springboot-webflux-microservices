package com.idea.springboot.webflux.app.controllers;

import com.idea.springboot.webflux.app.models.dtos.ProductDTO;
import com.idea.springboot.webflux.app.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/clients")
public class ClientRestController {

    @Autowired
    private ProductService productService;

    @GetMapping("/")
    public Mono<ResponseEntity<Flux<ProductDTO>>> getAll() {
        return Mono.just(ResponseEntity.ok(productService.getAll()));
    }
}
