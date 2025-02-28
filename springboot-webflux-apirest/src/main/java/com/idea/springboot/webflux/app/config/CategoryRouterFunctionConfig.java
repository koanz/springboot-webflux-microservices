package com.idea.springboot.webflux.app.config;

import com.idea.springboot.webflux.app.handler.CategoryHandler;
import com.idea.springboot.webflux.app.models.dtos.CategoryDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class CategoryRouterFunctionConfig {

    @Bean
    @RouterOperations({
            @RouterOperation(path = "/v2/categories/",
                    produces = { MediaType.APPLICATION_JSON_VALUE },
                    method = RequestMethod.GET,
                    beanClass = CategoryHandler.class,
                    beanMethod = "getAll",
                    operation = @Operation(
                            operationId = "getAll",
                            summary = "Returns a list of all categories available",
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(schema = @Schema(implementation = CategoryDTO.class))),
                                    @ApiResponse(responseCode = "400", description = "Invalid request"),
                                    @ApiResponse(responseCode = "404", description = "Categories not found")
                            })
            ),
            @RouterOperation(path = "/v2/categories/{id}",
                    produces = { MediaType.APPLICATION_JSON_VALUE },
                    method = RequestMethod.GET,
                    beanClass = CategoryHandler.class,
                    beanMethod = "findById",
                    operation = @Operation(
                            operationId = "findById",
                            summary = "Find a category by ID",
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(schema = @Schema(implementation = CategoryDTO.class))),
                                    @ApiResponse(responseCode = "400", description = "Invalid Category ID supplied"),
                                    @ApiResponse(responseCode = "404", description = "Category not found")
                            },
                            parameters = @Parameter(in = ParameterIn.PATH, name = "id", description = "Category ID")
                    )
            ),
            @RouterOperation(
                    path = "/v2/categories/create",
                    produces = { MediaType.APPLICATION_JSON_VALUE },
                    method = RequestMethod.POST,
                    beanClass = CategoryHandler.class,
                    beanMethod = "save",
                    operation = @Operation(
                            operationId = "create",
                            summary = "Create a new category",
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Successful operation",
                                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    schema = @Schema(implementation = CategoryDTO.class))),
                                    @ApiResponse(responseCode = "400", description = "Invalid Category details supplied")
                            },
                            requestBody = @RequestBody(
                                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(implementation = CategoryDTO.class))
                            )
                    )
            ),
            @RouterOperation(path = "/v2/categories/update/{id}",
                    produces = { MediaType.APPLICATION_JSON_VALUE },
                    method = RequestMethod.PUT,
                    beanClass = CategoryHandler.class,
                    beanMethod = "update",
                    operation = @Operation(
                            operationId = "update",
                            summary = "Update an existing category",
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(schema = @Schema(implementation = CategoryDTO.class))),
                                    @ApiResponse(responseCode = "400", description = "Invalid Category ID supplied"),
                                    @ApiResponse(responseCode = "404", description = "Category not found")
                            },
                            requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = CategoryDTO.class))),
                            parameters = @Parameter(in = ParameterIn.PATH, name = "id", description = "Category ID")
                    )
            ),
            @RouterOperation(path = "/v2/categories/delete/{id}",
                    produces = { MediaType.APPLICATION_JSON_VALUE },
                    method = RequestMethod.DELETE,
                    beanClass = CategoryHandler.class,
                    beanMethod = "delete",
                    operation = @Operation(
                            operationId = "delete",
                            summary = "Delete a category by ID",
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "Boolean")),
                                    @ApiResponse(responseCode = "400", description = "Invalid Category ID supplied"),
                                    @ApiResponse(responseCode = "404", description = "Category not found")
                            },
                            parameters = @Parameter(in = ParameterIn.PATH, name = "id", description = "Category ID")
                    )
            )
    })
    public RouterFunction<ServerResponse> categoryRoutes(CategoryHandler categoryHandler) {
        return RouterFunctions.route()
                .GET("/api/v2/categories", categoryHandler::getAll)
                .GET("/api/v2/categories/", categoryHandler::getAll)
                .GET("/api/v2/categories/{id}", categoryHandler::findById)
                .POST("/api/v2/categories/create", categoryHandler::save)
                .PUT("/api/v2/categories/update/{id}", categoryHandler::update)
                .DELETE("/api/v2/categories/delete/{id}", categoryHandler::delete)
                .build();
    }
}
