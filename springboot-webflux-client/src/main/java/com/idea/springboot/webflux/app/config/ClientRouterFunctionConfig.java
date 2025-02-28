package com.idea.springboot.webflux.app.config;

import com.idea.springboot.webflux.app.handler.ClientHandler;
import com.idea.springboot.webflux.app.models.dtos.ProductDTO;
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
public class ClientRouterFunction {
    @Bean
    @RouterOperations({
            @RouterOperation(path = "/v2/clients/",
                    produces = { MediaType.APPLICATION_JSON_VALUE },
                    method = RequestMethod.GET,
                    beanClass = ClientHandler.class,
                    beanMethod = "getAll",
                    operation = @Operation(
                            operationId = "getAll",
                            summary = "Returns a list of all products available",
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(schema = @Schema(implementation = ProductDTO.class))),
                                    @ApiResponse(responseCode = "400", description = "Invalid request"),
                                    @ApiResponse(responseCode = "404", description = "Products not found")
                            })
            ),
            @RouterOperation(path = "/v2/clients/{id}",
                    produces = { MediaType.APPLICATION_JSON_VALUE },
                    method = RequestMethod.GET,
                    beanClass = ClientHandler.class,
                    beanMethod = "findById",
                    operation = @Operation(
                            operationId = "findById",
                            summary = "Find a product by ID",
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(schema = @Schema(implementation = ProductDTO.class))),
                                    @ApiResponse(responseCode = "400", description = "Invalid Product ID supplied"),
                                    @ApiResponse(responseCode = "404", description = "Product not found")
                            },
                            parameters = @Parameter(in = ParameterIn.PATH, name = "id", description = "Product ID")
                    )
            ),
            @RouterOperation(
                    path = "/v2/clients/create",
                    produces = { MediaType.APPLICATION_JSON_VALUE },
                    method = RequestMethod.POST,
                    beanClass = ClientHandler.class,
                    beanMethod = "save",
                    operation = @Operation(
                            operationId = "create",
                            summary = "Create a new product",
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Successful operation",
                                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    schema = @Schema(implementation = ProductDTO.class))),
                                    @ApiResponse(responseCode = "400", description = "Invalid Product details supplied")
                            },
                            requestBody = @RequestBody(
                                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(implementation = ProductDTO.class))
                            )
                    )
            ),
            @RouterOperation(path = "/v2/clients/update/{id}",
                    produces = { MediaType.APPLICATION_JSON_VALUE },
                    method = RequestMethod.PUT,
                    beanClass = ClientHandler.class,
                    beanMethod = "update",
                    operation = @Operation(
                            operationId = "update",
                            summary = "Update an existing product",
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(schema = @Schema(implementation = ProductDTO.class))),
                                    @ApiResponse(responseCode = "400", description = "Invalid Product ID supplied"),
                                    @ApiResponse(responseCode = "404", description = "Product not found")
                            },
                            requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = ProductDTO.class))),
                            parameters = @Parameter(in = ParameterIn.PATH, name = "id", description = "Product ID")
                    )
            ),
            @RouterOperation(path = "/v2/clients/delete/{id}",
                    produces = { MediaType.APPLICATION_JSON_VALUE },
                    method = RequestMethod.DELETE,
                    beanClass = ClientHandler.class,
                    beanMethod = "delete",
                    operation = @Operation(
                            operationId = "delete",
                            summary = "Delete a product by ID",
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "Boolean")),
                                    @ApiResponse(responseCode = "400", description = "Invalid Product ID supplied"),
                                    @ApiResponse(responseCode = "404", description = "Product not found")
                            },
                            parameters = @Parameter(in = ParameterIn.PATH, name = "id", description = "Product ID")
                    )
            )
    })
    public RouterFunction<ServerResponse> productRoutes(ClientHandler clientHandler) {
        return RouterFunctions.route()
                .GET("/api/v2/clients/", clientHandler::getAll)
                .GET("/api/v2/clients/{id}", clientHandler::findById)
                /*.POST("/api/v2/clients/create", clientHandler::save)
                .PUT("/api/v2/clients/update/{id}", clientHandler::update)
                .DELETE("/api/v2/clients/delete/{id}", clientHandler::delete)*/
                .build();
    }
}
