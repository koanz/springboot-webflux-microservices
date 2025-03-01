package com.idea.springboot.webflux.app.handler;

import com.idea.springboot.webflux.app.models.responses.MessageResponse;
import com.idea.springboot.webflux.app.models.dtos.ProductDTO;
import com.idea.springboot.webflux.app.services.ProductService;
import com.idea.springboot.webflux.app.validations.CustomRequestProductValidator;
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
@Tag(name = "Products API V2 (Router function)")
public class ProductHandler {
    @Autowired
    private ProductService service;

    @Autowired
    private MessageSource messageSource;

    @Value("${product.field.name.message}")
    private String FIELD_NAME;

    private final Validator validator = new CustomRequestProductValidator();

    public Mono<ServerResponse> getAll(ServerRequest request) {
        return ServerResponse.ok().body(service.getAll(), ProductDTO.class);
    }

    public Mono<ServerResponse> findById(ServerRequest request) {
        return service.findById(request.pathVariable("id"))
                .flatMap(product -> ServerResponse.ok().bodyValue(product))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> save(ServerRequest request) {
        return request.bodyToMono(ProductDTO.class).flatMap(productDTO -> {
            Errors errors = new BeanPropertyBindingResult(productDTO, ProductDTO.class.getName());
            validator.validate(productDTO, errors);

            if (errors.hasErrors()) {
                List<ValidationErrorResponse.ValidationError> validationErrors = new ArrayList<>();
                errors.getAllErrors().forEach(error -> {
                    String field = ((FieldError) error).getField();
                    String errorMessage = error.getDefaultMessage();
                    validationErrors.add(new ValidationErrorResponse.ValidationError(field, errorMessage));
                });

                return ServerResponse.badRequest().bodyValue(new ValidationErrorResponse("Validation failed", validationErrors));
            }

            return service.save(productDTO).flatMap(savedProduct -> ServerResponse.ok().bodyValue(savedProduct));
        });
    }

    public Mono<ServerResponse> update(ServerRequest request) {
        Mono<ProductDTO> productDtoMono = request.bodyToMono(ProductDTO.class);
        return productDtoMono.flatMap(productDTO -> service.update(request.pathVariable("id"), productDTO)
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
