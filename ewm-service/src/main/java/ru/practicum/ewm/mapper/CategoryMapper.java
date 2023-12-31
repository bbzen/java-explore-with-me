package ru.practicum.ewm.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.ewm.model.Category;
import ru.practicum.ewm.model.dto.CategoryDto;
import ru.practicum.ewm.model.dto.NewCategoryDto;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    @Mapping(target = "id", ignore = true)
    Category toCategory(CategoryDto dto);

    Category toCategory(NewCategoryDto dto);

    CategoryDto toCategoryDto(Category cat);
}
