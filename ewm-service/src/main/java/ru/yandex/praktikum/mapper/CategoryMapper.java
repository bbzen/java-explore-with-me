package ru.yandex.praktikum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.yandex.praktikum.model.CategoryDto;
import ru.yandex.praktikum.model.NewCategoryDto;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    CategoryDto toCategoryDto(NewCategoryDto dto);
}
