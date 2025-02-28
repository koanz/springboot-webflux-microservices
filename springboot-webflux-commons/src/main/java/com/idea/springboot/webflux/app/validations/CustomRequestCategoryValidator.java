package com.idea.springboot.webflux.app.validations;

import com.idea.springboot.webflux.app.models.dtos.CategoryDTO;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class CustomRequestCategoryValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return CategoryDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "field.required", "Field name is required.");
    }
}
