package com.product.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.product.api.dto.ApiResponse;
import com.product.api.entity.Category;
import com.product.api.service.SvcCategory;
import com.product.exception.ApiException;

@RestController
@RequestMapping("/category")
public class CtrlCategory {

	private final SvcCategory categoryService;

	@Autowired
	public CtrlCategory(SvcCategory categoryService) {
		this.categoryService = categoryService;
	}

	@GetMapping
	public ResponseEntity<List<Category>> listCategories() throws Exception{
		List<Category> categories = categoryService.listCategories();

		return new ResponseEntity<>(categories, HttpStatus.OK);
	}

	@GetMapping(path = "/{category_id}")
	public ResponseEntity<Category> readCategory(@PathVariable(value = "category_id") int categoryId) {
		Category category = categoryService.readCategory(categoryId);

		return new ResponseEntity<>(category, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<ApiResponse> createCategory(@Valid @RequestBody Category category,
													  BindingResult bindingResult) {
		if(bindingResult.hasErrors()) {
			throw new ApiException(HttpStatus.BAD_REQUEST,
					bindingResult.getAllErrors().get(0).getDefaultMessage());
		}

		ApiResponse isCreated = categoryService.createCategory(category.getCategory());

		return new ResponseEntity<>(isCreated, HttpStatus.CREATED);
	}

	@PutMapping(path = "/{category_id}")
	public ResponseEntity<ApiResponse> updateCategory(@PathVariable(value = "category_id") int categoryId,
													  @Valid @RequestBody Category category,
													  BindingResult bindingResult) {
		if(bindingResult.hasErrors()) {
			throw new ApiException(HttpStatus.BAD_REQUEST,
					bindingResult.getAllErrors().get(0).getDefaultMessage());
		}

		ApiResponse isUpdated = categoryService.updateCategory(categoryId, category.getCategory());

		return new ResponseEntity<>(isUpdated, HttpStatus.OK);
	}

	@DeleteMapping("/{category_id}")
	public ResponseEntity<ApiResponse> deleteCategory(@PathVariable int category_id) {
		ApiResponse deletedCategory = categoryService.deleteCategory(category_id);

		return new ResponseEntity<>(deletedCategory, HttpStatus.OK);
	}
}
