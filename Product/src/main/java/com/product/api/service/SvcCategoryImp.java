package com.product.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.product.api.dto.ApiResponse;
import com.product.api.entity.Category;
import com.product.api.repository.RepoCategory;
import com.product.exception.ApiException;

@Service
public class SvcCategoryImp implements SvcCategory {

	private final RepoCategory categoryRepository;

	@Autowired
	public SvcCategoryImp(RepoCategory categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	@Override
	public List<Category> listCategories() {
		return categoryRepository.findAllCategory();
	}

	@Override
	public Category readCategory(int categoryId) {
		Category category = categoryRepository.findByCategoryId(categoryId);

		if(category != null) {
			return category;
		} else {
			throw new ApiException(HttpStatus.NOT_FOUND, "category does not exist");
		}
	}

	@Override
	public ApiResponse createCategory(String category) {
		try{
			Category categoryFound = categoryRepository.findByCategory(category);

			if(categoryFound != null) {
				if(categoryFound.getStatus() == 0) {
					categoryRepository.activeCategory(category);
					return new ApiResponse("category has been activated");
				} else {
					throw new ApiException(HttpStatus.BAD_REQUEST, "category already exists");
				}
			} else {
				categoryRepository.createCategory(category);
				return new ApiResponse("category created");
			}
		}catch(DataIntegrityViolationException e) {
			if(e.getLocalizedMessage().contains("category")) {
				throw new ApiException(HttpStatus.BAD_REQUEST, "category rfc already exist");
			}
		}
		return new ApiResponse("category created");
	}

	@Override
	public ApiResponse updateCategory(int categoryId, String category) {
		try {
			Category foundCategory = categoryRepository.findByCategoryId(categoryId);

			if(foundCategory == null) {
				throw new ApiException(HttpStatus.NOT_FOUND, "category does not exist");
			} else {
				if(foundCategory.getStatus() == 1) {
					if(categoryRepository.findByCategory(category) == null) {
						categoryRepository.updateCategory(categoryId, category);
						return new ApiResponse("category updated");
					}else {
						throw new ApiException(HttpStatus.BAD_REQUEST, "category already exists");
					}
				} else {
					throw new ApiException(HttpStatus.BAD_REQUEST, "category is not active");
				}
			}
		}catch(DataIntegrityViolationException e) {
			if(e.getLocalizedMessage().contains("category")) {
				throw new ApiException(HttpStatus.BAD_REQUEST, "category rfc already exists");
			}
		}
		return new ApiResponse("category updated");
	}

	@Override
	public ApiResponse deleteCategory(int categoryId) {
		Category category = categoryRepository.findByCategoryId(categoryId);

		if(category != null) {
			if(category.getStatus() == 1) {
				throw new ApiException(HttpStatus.BAD_REQUEST,
						"category cannot be removed if it has products");
			}else {
				categoryRepository.deleteCategoryById(categoryId);
				return new ApiResponse("category removed");
			}
		} else {
			throw new ApiException(HttpStatus.NOT_FOUND, "category does not exist");
		}
	}
}
