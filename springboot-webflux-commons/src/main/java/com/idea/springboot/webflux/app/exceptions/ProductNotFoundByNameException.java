package com.idea.springboot.webflux.app.exceptions;

public class ProductNotFoundByNameException extends RuntimeException {
    public ProductNotFoundByNameException(String name) {
        super("Product with name '" + name + "' not found");
    }
}
