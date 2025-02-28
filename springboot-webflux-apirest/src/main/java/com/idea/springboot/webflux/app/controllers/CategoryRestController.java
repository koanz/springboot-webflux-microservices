package com.idea.springboot.webflux.app.controllers;

import com.idea.springboot.webflux.app.models.MessageResponse;
import com.idea.springboot.webflux.app.models.dtos.CategoryDTO;
import com.idea.springboot.webflux.app.services.CategoryService;
import com.idea.springboot.webflux.app.validations.OnCreate;
import com.idea.springboot.webflux.app.validations.OnUpdate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.Locale;

@RestController
@RequestMapping("/api/v1/categories")
@Tag(name = "Categories API V1")
public class CategoryRestController {
    private static final Logger logger = LoggerFactory.getLogger(ProductRestController.class);

    @Value("${category.field.name.message}")
    private String FIELD_NAME;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/")
    @Operation(summary = "Returns a list of all categories available", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = CategoryDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "Categories not found")
    })
    public Mono<ResponseEntity<Flux<CategoryDTO>>> getAll() {
        return Mono.just(ResponseEntity.ok(categoryService.getAll()));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<CategoryDTO>> findById(@PathVariable String id) {
        return categoryService.findDTOById(id).map(ResponseEntity::ok);
    }

    @PostMapping("/create")
    public Mono<ResponseEntity<CategoryDTO>> create(@RequestBody @Validated(OnCreate.class) CategoryDTO request) {
        return categoryService.save(request).map(ResponseEntity::ok);
    }

    @PutMapping("/update/{id}")
    public Mono<ResponseEntity<CategoryDTO>> update(@PathVariable String id, @Validated(OnUpdate.class) @RequestBody CategoryDTO request) {
        return categoryService.update(id, request).map(ResponseEntity::ok);
    }

    @DeleteMapping("/delete/{id}")
    public Mono<ResponseEntity<MessageResponse>> delete(@PathVariable String id) {
        Date currentDate = new Date();

        return categoryService.delete(id)
                .then(Mono.just(ResponseEntity.ok(new MessageResponse(messageSource.getMessage("update.message", null, Locale.getDefault()), currentDate.toString()))));
    }
}
