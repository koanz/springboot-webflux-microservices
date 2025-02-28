package com.idea.springboot.webflux.app.exceptions;

public class CategoryNotFountByIdException extends RuntimeException {
    public CategoryNotFountByIdException(String id) {
        super("Category with id '" + id + "' not found");
    }
}
