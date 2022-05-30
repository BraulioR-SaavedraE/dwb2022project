package com.product.api.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.product.api.entity.Product;

import java.util.List;

@Repository
public interface RepoProduct extends JpaRepository<Product, Integer>{

	Product findByGtinAndStatus(@Param("gtin") String gtin, @Param("status") Integer status);

	@Query(value = "SELECT * FROM product", nativeQuery = true)
	List<Product> listProducts(@Param(value = "category_id") int categoryId);

	@Transactional
	@Modifying
	@Query(value = "UPDATE product SET category_id = :category_id WHERE product_id = :product_id",
			nativeQuery = true)
	void updateProductCategory(@Param(value = "product_id") int productId,
							   @Param(value = "category_id") int categoryId);

	@Query(value = "SELECT * FROM product WHERE product_id = :product_id", nativeQuery = true)
	Product findProductById(@Param(value = "product_id") int productId);

	@Query(value = "SELECT * FROM product WHERE category_id = :category_id", nativeQuery = true)
	Product findProductByCategoryId(@Param(value = "category_id") int categoryId);

	@Modifying
	@Transactional
	@Query(value ="UPDATE product "
			+ "SET gtin = :gtin, "
			+ "product = :product, "
			+ "description = :description, "
			+ "price = :price, "
			+ "stock = :stock, "
			+ "status = 1 "
			+ "WHERE product_id = :product_id", nativeQuery = true)
	Integer updateProduct(
			@Param("product_id") Integer product_id,
			@Param("gtin") String gtin,
			@Param("product") String product,
			@Param("description") String description,
			@Param("price") Double price,
			@Param("stock") Integer stock
	);

	@Modifying
	@Transactional
	@Query(value ="UPDATE product SET status = 0 WHERE product_id = :product_id AND status = 1", nativeQuery = true)
	Integer deleteProduct(@Param("product_id") Integer product_id);

	@Modifying
	@Transactional
	@Query(value ="UPDATE product "
			+ "SET stock = :stock"
			+ " WHERE product_id = :product_id", nativeQuery = true)
	Integer updateStock(
			@Param("product_id") Integer product_id,
			@Param("stock") Integer stock
	);
}
