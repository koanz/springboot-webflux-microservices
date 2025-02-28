package com.idea.springboot.webflux.app.exceptions;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String id) {
        super("Product with id '" + id + "' not found");
    }
}
