package com.idea.springboot.webflux.app.validations;

import com.idea.springboot.webflux.app.models.dtos.ProductDTO;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class CustomRequestProductValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return ProductDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "field.required", "Field name is required.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "price", "field.required", "Field price is required.");

        ProductDTO request = (ProductDTO) target;

        if(request.getCategory() == null) {
            errors.rejectValue("category", "field.required", "Field Category is required.");
        }

        if(request.getCategory() != null) {
            if(request.getCategory().getId() == null) {
                errors.rejectValue("id", "field.required", "Field Category id is required.");
            }
        }

        if (request.getPrice() != null) {
            if(request.getPrice() <= 0.0) {
                errors.rejectValue("price", "field.greater.than", "Field price must be greater than zero (0).");
            }
        }
    }
}
