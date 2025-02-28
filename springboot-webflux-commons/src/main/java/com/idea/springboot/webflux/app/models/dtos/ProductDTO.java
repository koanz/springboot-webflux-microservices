package com.idea.springboot.webflux.app.models.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.idea.springboot.webflux.app.validations.OnProductCreate;
import com.idea.springboot.webflux.app.validations.OnProductUpdate;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Product Data Transfer Object")
public class ProductDTO {
    @Schema(description = "Product ID", example = "123")
    private String id;

    @Schema(description = "Product Name", example = "Laptop")
    @NotEmpty(message = "Product name cannot be empty.", groups = {OnProductCreate.class, OnProductUpdate.class})
    private String name;

    @Schema(description = "Product Price", example = "1999.99")
    @NotNull(message = "Product price cannot be null.", groups = {OnProductCreate.class, OnProductUpdate.class})
    @Positive(message = "Product price must be greater than zero (0).", groups = {OnProductCreate.class, OnProductUpdate.class})
    private Double price;

    @Schema(description = "Product Category", example = "category")
    @NotNull(message = "Field category cannot be empty.", groups = {OnProductCreate.class, OnProductUpdate.class})
    @Valid
    private CategoryDTO category;

    @Schema(description = "Product Created At", example = "2025-02-01")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("created_at")
    private Date createdAt;

    public ProductDTO() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public CategoryDTO getCategory() {
        return category;
    }

    public void setCategory(CategoryDTO category) {
        this.category = category;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
