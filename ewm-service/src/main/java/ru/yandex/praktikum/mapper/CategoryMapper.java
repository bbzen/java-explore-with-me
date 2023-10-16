package ru.yandex.praktikum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.yandex.praktikum.model.Category;
import ru.yandex.praktikum.model.dto.CategoryDto;
import ru.yandex.praktikum.model.dto.NewCategoryDto;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    @Mapping(target = "id", ignore = true)
    Category toCategory(NewCategoryDto dto);

    CategoryDto toCategoryDto(Category cat);

    default Category toCategory(Long category) {
        if (category == null) {
            return null;
        }
        Category result = new Category();
        result.setId(category);
        return result;
    }
}
