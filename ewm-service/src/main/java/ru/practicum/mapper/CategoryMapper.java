package ru.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.model.Category;
import ru.practicum.model.dto.CategoryDto;
import ru.practicum.model.dto.NewCategoryDto;

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
