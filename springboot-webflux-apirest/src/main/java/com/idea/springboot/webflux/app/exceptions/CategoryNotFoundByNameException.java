package com.idea.springboot.webflux.app.exceptions;

public class CategoryNotFoundByNameException extends RuntimeException {
    public CategoryNotFoundByNameException(String name) {
        super("Category with name '" + name + "' not found");
    }
}
