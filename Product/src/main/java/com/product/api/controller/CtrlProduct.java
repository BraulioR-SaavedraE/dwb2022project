package com.product.api.controller;

import javax.validation.Valid;

import com.product.api.dto.ProductDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.product.api.dto.ApiResponse;
import com.product.api.entity.Product;
import com.product.api.service.SvcProduct;
import com.product.exception.ApiException;

import java.util.List;

@RestController
@RequestMapping("/product")
public class CtrlProduct {

	private final SvcProduct productService;

	@Autowired
	CtrlProduct(SvcProduct productService) {
		this.productService = productService;
	}

	@GetMapping("/{gtin}")
	public ResponseEntity<Product> getProduct(@PathVariable("gtin") String gtin){
		return new ResponseEntity<>(productService.getProduct(gtin), HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<ApiResponse> createProduct(@Valid @RequestBody Product in, BindingResult bindingResult){
		if(bindingResult.hasErrors())
			throw new ApiException(HttpStatus.BAD_REQUEST, bindingResult.getAllErrors().get(0).getDefaultMessage());
		return new ResponseEntity<>(productService.createProduct(in),HttpStatus.OK);
	}

	@GetMapping(path = "/category/{category_id}")
	ResponseEntity<List<ProductDTO>> listProducts(@PathVariable(value = "category_id")
														  int categoryId) throws Exception{
		List<ProductDTO> products = productService.listProducts(categoryId);

		return new ResponseEntity<>(products, HttpStatus.OK);
	}

	@PutMapping(path = "/{product_id}")
	ResponseEntity<ApiResponse> updateProductCategory(@PathVariable(value = "product_id") int productId,
													  @RequestBody Product product) {
		try {
			ApiResponse response = productService.updateProductCategory(productId, product.getCategory_id());
		}catch(DataIntegrityViolationException e) {
			if(e.getLocalizedMessage().contains("gtin"))
				throw new ApiException(HttpStatus.BAD_REQUEST, "product gtin already exist");
			if(e.getLocalizedMessage().contains("product"))
				throw new ApiException(HttpStatus.BAD_GATEWAY, "product name already exist");
		}
		return new ResponseEntity<>(new ApiResponse("product updated"), HttpStatus.OK);
	}

	@PutMapping(path = "/{gtin}/{quantity}")
	ResponseEntity<ApiResponse> updateStock(@PathVariable(value = "gtin") String gtin,
												@PathVariable(value = "quantity") int quantity) {
		try {
			ApiResponse response = productService.updateStock(gtin, quantity);
		}catch(DataIntegrityViolationException e) {
			if(e.getLocalizedMessage().contains("stock"))
				throw new ApiException(HttpStatus.BAD_REQUEST, "we have not enough stock");
		}
		return new ResponseEntity<>(new ApiResponse("stock updated"), HttpStatus.OK);
	}
}
