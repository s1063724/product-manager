package com.example.productmanagement.service;

import com.example.productmanagement.controller.ProductController;
import com.example.productmanagement.model.Product;
import com.example.productmanagement.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    // 獲取所有商品
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // 搜尋商品
    public List<Product> searchProducts(String query) {
        return productRepository.findByNameContainingIgnoreCase(query);
    }

    // 添加商品
    public Product addProduct(Product product) throws ProductController.ProductAlreadyExistsException {
        // 檢查商品是否已存在
        if (productRepository.findByName(product.getName()).isPresent()) {
            throw new ProductController.ProductAlreadyExistsException("商品已存在");
        }
        // 保存商品並返回
        return productRepository.save(product);
    }

    // 編輯商品
    public void updateProduct(Long id, Product product) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("商品未找到"));
        existingProduct.setName(product.getName());
        existingProduct.setSpecification(product.getSpecification());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setImageUrl(product.getImageUrl());
        productRepository.save(existingProduct);
    }

    // 刪除商品
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}