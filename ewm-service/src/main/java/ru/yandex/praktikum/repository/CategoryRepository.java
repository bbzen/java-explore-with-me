package ru.yandex.praktikum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.yandex.praktikum.model.Category;

import javax.transaction.Transactional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Transactional
    @Modifying
    @Query("UPDATE Category c SET c.name = ?1 WHERE c.id = ?2")
    int updateNameById(@Param("name") String name, @Param("id") Long id);
}
