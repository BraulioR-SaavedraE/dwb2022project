package com.product.api.service;

import com.product.api.dto.ApiResponse;
import com.product.api.dto.ProductDTO;
import com.product.api.entity.Product;

import java.util.List;

public interface SvcProduct {
	Product getProduct(String gtin);
	List<ProductDTO> listProducts(int categoryId) throws Exception;
	ApiResponse createProduct(Product in);
	ApiResponse updateProductCategory(int productId, int categoryId);
	ApiResponse updateProduct(Product in, Integer id);
	ApiResponse deleteProduct(Integer id);
	ApiResponse updateStock(String gtin, Integer quantity);

}
