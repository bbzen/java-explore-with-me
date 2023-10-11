package ru.yandex.praktikum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.yandex.praktikum.model.CategoryDto;

public interface CategoryRepository extends JpaRepository<CategoryDto, Long> {

    @Query(value = "update categories SET name = ?1 WHERE id = ?2", nativeQuery = true)
    CategoryDto updateById(String newCategoryName, Long categoryId);
}
