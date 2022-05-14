package com.product.api.service;

import com.product.api.dto.ProductDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.product.api.dto.ApiResponse;
import com.product.api.entity.Product;
import com.product.api.repository.RepoCategory;
import com.product.api.repository.RepoProduct;
import com.product.exception.ApiException;

import java.util.LinkedList;
import java.util.List;

@Service
public class SvcProductImp implements SvcProduct {

	@Autowired
	private RepoProduct productRepository;
	@Autowired
	private RepoCategory categoryRepository;

	@Override
	public Product getProduct(String gtin) {
		Product product = productRepository.findByGtinAndStatus(gtin,1);
		if (product != null) {
			product.setCategory(categoryRepository.getCategory(product.getCategory_id()));
			return product;
		}else
			throw new ApiException(HttpStatus.NOT_FOUND, "product does not exist");
	}

	@Override
	public List<ProductDTO> listProducts(int categoryId) {
		List<Product> foundProducts = productRepository.listProducts(categoryId);
		LinkedList<ProductDTO> productsDTO= new LinkedList<>();

		for(Product p : foundProducts) {
			ProductDTO productDTO = new ProductDTO(p.getProduct_id(),
					p.getGtin(), p.getProduct(), p.getPrice());
			productsDTO.add(productDTO);
		}
		return productsDTO;
	}

	@Override
	public ApiResponse createProduct(Product in) {
		Product product = productRepository.findByGtinAndStatus(in.getGtin(),0);
		if(product != null) {
			updateProduct(in,product.getProduct_id());
			return new ApiResponse("product activated");
		}else {
			try {
				in.setStatus(1);
				productRepository.save(in);
			}catch (DataIntegrityViolationException e) {
				if (e.getLocalizedMessage().contains("gtin"))
					throw new ApiException(HttpStatus.BAD_REQUEST, "product gtin already exist");
				if (e.getLocalizedMessage().contains("product"))
					throw new ApiException(HttpStatus.BAD_REQUEST, "product name already exist");
			}
			return new ApiResponse("product created");
		}
	}

	@Override
	public ApiResponse updateProduct(Product in, Integer id) {
		try {
			productRepository.updateProduct(id, in.getGtin(), in.getProduct(), in.getDescription(), in.getPrice(), in.getStock());
		}catch (DataIntegrityViolationException e) {
			if (e.getLocalizedMessage().contains("gtin"))
				throw new ApiException(HttpStatus.BAD_REQUEST, "product gtin already exist");
			if (e.getLocalizedMessage().contains("product"))
				throw new ApiException(HttpStatus.BAD_REQUEST, "product name already exist");
		}
		return new ApiResponse("product updated");
	}

	@Override
	public ApiResponse updateProductCategory(int productId, int categoryId) {
		Product foundProduct = productRepository.findProductById(productId);

		if(foundProduct== null) {
			throw new ApiException(HttpStatus.BAD_REQUEST, "product category cannot be updated");
		} else {
			if(foundProduct.getStatus() == 1) {
				if(categoryRepository.findByCategoryId(categoryId) == null) {
					throw new ApiException(HttpStatus.NOT_FOUND, "category not found");
				}else {
					if(foundProduct.getCategory_id() == categoryId) {
						throw new ApiException(HttpStatus.BAD_REQUEST, "product category cannot be updated");
					}else {
						productRepository.updateProductCategory(productId, categoryId);
						return new ApiResponse("product category updated");
					}
				}
			} else {
				throw new ApiException(HttpStatus.BAD_REQUEST, "product category cannot be updated");
			}
		}
	}

	@Override
	public ApiResponse deleteProduct(Integer id) {
		if (productRepository.deleteProduct(id) > 0)
			return new ApiResponse("product removed");
		else
			throw new ApiException(HttpStatus.BAD_REQUEST, "product cannot be deleted");
	}
}
