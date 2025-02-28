package com.idea.springboot.webflux.app.mappers;

import com.idea.springboot.webflux.app.models.documents.Category;
import com.idea.springboot.webflux.app.models.dtos.CategoryDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    Category toEntity(CategoryDTO categoryDTO);

    CategoryDTO toDTO(Category category);
}
