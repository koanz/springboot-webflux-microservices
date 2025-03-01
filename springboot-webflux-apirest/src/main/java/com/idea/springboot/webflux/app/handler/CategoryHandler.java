package com.idea.springboot.webflux.app.handler;

import com.idea.springboot.webflux.app.models.responses.MessageResponse;
import com.idea.springboot.webflux.app.models.dtos.CategoryDTO;
import com.idea.springboot.webflux.app.models.dtos.ProductDTO;
import com.idea.springboot.webflux.app.services.CategoryService;
import com.idea.springboot.webflux.app.validations.CustomRequestCategoryValidator;
import com.idea.springboot.webflux.app.validations.ValidationErrorResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Component
@Tag(name = "Categories API V2 (Router function)")
public class CategoryHandler {
    @Autowired
    private CategoryService service;

    @Autowired
    private MessageSource messageSource;

    @Value("${category.field.name.message}")
    private String FIELD_NAME;

    private final Validator validator = new CustomRequestCategoryValidator();

    public Mono<ServerResponse> getAll(ServerRequest request) {
        return ServerResponse.ok().body(service.getAll(), ProductDTO.class);
    }

    public Mono<ServerResponse> findById(ServerRequest request) {
        return service.findById(request.pathVariable("id"))
                .flatMap(category -> ServerResponse.ok().bodyValue(category))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> save(ServerRequest request) {
        return request.bodyToMono(CategoryDTO.class).flatMap(categoryDTO -> {
            Errors errors = new BeanPropertyBindingResult(categoryDTO, CategoryDTO.class.getName());
            validator.validate(categoryDTO, errors);

            if (errors.hasErrors()) {
                List<ValidationErrorResponse.ValidationError> validationErrors = new ArrayList<>();
                errors.getAllErrors().forEach(error -> {
                    String field = ((FieldError) error).getField();
                    String errorMessage = error.getDefaultMessage();
                    validationErrors.add(new ValidationErrorResponse.ValidationError(field, errorMessage));
                });

                return ServerResponse.badRequest().bodyValue(new ValidationErrorResponse("Validation failed", validationErrors));
            }

            return service.save(categoryDTO).flatMap(savedCategory -> {
                return ServerResponse.ok().bodyValue(savedCategory);
            });
        });
    }

    public Mono<ServerResponse> update(ServerRequest request) {
        Mono<CategoryDTO> categoryDtoMono = request.bodyToMono(CategoryDTO.class);
        return categoryDtoMono.flatMap(categoryDTO -> service.update(request.pathVariable("id"), categoryDTO)
                .flatMap(updatedProduct -> {
                    return ServerResponse.ok().bodyValue(updatedProduct);
                }));
    }

    public Mono<ServerResponse> delete(ServerRequest request) {
        return service.delete(request.pathVariable("id"))
                .then(ServerResponse.ok().bodyValue(new MessageResponse(
                        messageSource.getMessage("delete.message", null, Locale.getDefault()),
                        new Date().toString())));
    }
}
