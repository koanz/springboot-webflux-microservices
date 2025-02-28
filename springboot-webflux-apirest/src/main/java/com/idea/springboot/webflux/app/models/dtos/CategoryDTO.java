package com.idea.springboot.webflux.app.models.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.idea.springboot.webflux.app.validations.OnCreate;
import com.idea.springboot.webflux.app.validations.OnProductCreate;
import com.idea.springboot.webflux.app.validations.OnUpdate;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Category Data Transfer Object")
public class CategoryDTO {

    @Schema(description = "Product ID", example = "123")
    @NotNull(message = "Category id cannot be empty.", groups = OnProductCreate.class)
    private String id;

    @Schema(description = "Product Name", example = "technology")
    @NotEmpty(message = "Category name cannot be empty.", groups = {OnCreate.class, OnUpdate.class})
    private String name;

    @Schema(description = "Product Created At", example = "2025-02-01")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("created_at")
    private Date createdAt;

    public CategoryDTO() {
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
