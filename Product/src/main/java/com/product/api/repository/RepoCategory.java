package com.product.api.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.product.api.entity.Category;

@Repository
public interface RepoCategory extends JpaRepository<Category, Integer>{

	@Query(value = "SELECT * FROM category WHERE status = 1", nativeQuery = true)
	List<Category> findAllCategory();

	@Query(value ="SELECT * FROM category WHERE category_id = :category_id AND status = 1", nativeQuery = true)
	Category getCategory(Integer category_id);

	@Query(value = "SELECT * FROM category WHERE category_id = :category_id",
			nativeQuery = true)
	Category findByCategoryId(@Param(value = "category_id") Integer categoryId);

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO category (category, status) VALUES (:category, 1)",
			nativeQuery = true)
	void createCategory(@Param(value = "category") String category);

	@Query(value = "SELECT * FROM category WHERE category = :category",
			nativeQuery = true)
	Category findByCategory(@Param(value = "category") String category);

	@Transactional
	@Modifying
	@Query(value = "UPDATE category SET category = :category WHERE category_id = :category_id",
			nativeQuery = true)
	void updateCategory(@Param(value = "category_id") int categoryId,
						@Param(value = "category") String category);

	@Transactional
	@Modifying
	@Query(value = "UPDATE category SET status = 1 WHERE category = :category",
			nativeQuery = true)
	void activeCategory(@Param(value = "category") String category);

	@Transactional
	@Modifying
	@Query(value = "DELETE FROM category where category_id = :category_id",
			nativeQuery = true)
	void deleteCategoryById(@Param(value = "category_id") int categoryId);
}
