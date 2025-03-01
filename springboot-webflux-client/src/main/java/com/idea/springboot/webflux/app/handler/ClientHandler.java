package com.idea.springboot.webflux.app.handler;

import com.idea.springboot.webflux.app.models.dtos.ProductDTO;
import com.idea.springboot.webflux.app.models.responses.MessageResponse;
import com.idea.springboot.webflux.app.services.ProductService;
import com.idea.springboot.webflux.app.validations.CustomRequestProductValidator;
import com.idea.springboot.webflux.app.validations.ValidationErrorResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@Tag(name = "Clients API V2 (Router function)")
public class ClientHandler {
    @Autowired
    private ProductService productService;

    private final Validator validator = new CustomRequestProductValidator();


    public Mono<ServerResponse> getAll(ServerRequest request) {
        return ServerResponse.ok().body(productService.getAll(), ProductDTO.class);
    }

    public Mono<ServerResponse> findById(ServerRequest request) {
        return productService.findById(request.pathVariable("id"))
                .flatMap(product -> ServerResponse.ok().bodyValue(product))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> save(ServerRequest request) {
        Mono<ProductDTO> productDtoMono = request.bodyToMono(ProductDTO.class);

        return productDtoMono.flatMap(p -> {
            return productService.save(p);
        }).flatMap(p -> ServerResponse.created(URI.create("/api/v2/client/".concat(p.getId())))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(p))
                .onErrorResume(WebClientResponseException.class, error -> {
                    if (error.getStatusCode() == HttpStatus.BAD_REQUEST) {
                        return ServerResponse.badRequest()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(error.getResponseBodyAsString());
                    }

                    if (error.getStatusCode() == HttpStatus.NOT_FOUND) {
                        return ServerResponse.status(HttpStatus.NOT_FOUND)
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(error.getResponseBodyAsString());
                    }

                    return Mono.error(error);
                });
    }

    public Mono<ServerResponse> update(ServerRequest request) {
        Mono<ProductDTO> productDtoMono = request.bodyToMono(ProductDTO.class);
        return productDtoMono.flatMap(productDTO -> {
            // Validación de campos
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

            return productService.update(request.pathVariable("id"), productDTO)
                    .flatMap(updatedProduct -> {
                        return ServerResponse.ok().bodyValue(updatedProduct);
                    });
        });
    }

    public Mono<ServerResponse> delete(ServerRequest request) {
        return productService.delete(request.pathVariable("id"))
                .then(ServerResponse.ok().bodyValue(new MessageResponse("Successfully deleted", new Date().toString())))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

}
