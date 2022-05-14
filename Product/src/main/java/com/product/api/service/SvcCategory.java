package com.product.api.service;

import java.util.List;

import com.product.api.dto.ApiResponse;
import com.product.api.entity.Category;

public interface SvcCategory {

	List<Category> listCategories() throws Exception;

	Category readCategory(int categoryId);

	ApiResponse createCategory(String category);

	ApiResponse updateCategory(int categoryId, String category);

	ApiResponse deleteCategory(int categoryId);

}
