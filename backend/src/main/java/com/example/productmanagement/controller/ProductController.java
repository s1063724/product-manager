package com.example.productmanagement.controller;

import com.example.productmanagement.model.Product;
import com.example.productmanagement.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "http://localhost:8081") // 允許的前端域名
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        logger.info("收到獲取所有商品的請求");
        List<Product> products = productService.getAllProducts();
        logger.info("成功獲取所有商品，數量: {}", products.size());
        return ResponseEntity.ok(products);
    }
    // 搜尋商品
    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String query) {
        List<Product> products = productService.searchProducts(query);
        return ResponseEntity.ok(products);
    }

    // 編輯商品
    @PutMapping("/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        productService.updateProduct(id, product);
        return ResponseEntity.ok("商品更新成功");
    }

    @PostMapping("/add")
    public ResponseEntity<?> addProduct(@RequestBody Product product) {
        logger.info("收到添加商品的需求：{}", product);
        try {
            Product savedProduct = productService.addProduct(product);
            logger.info("商品添加成功：{}", savedProduct);
            return ResponseEntity.ok(savedProduct);
        } catch (ProductAlreadyExistsException e) {
            logger.warn("添加商品失败：{}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("商品已存在");
        } catch (RuntimeException e) {
            logger.error("添加商品时发生错误：{}", e.getMessage());
            return ResponseEntity.badRequest().body("添加商品失败：" + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        logger.info("收到刪除商品的請求，商品 ID: {}", id);
        try {
            productService.deleteProduct(id);
            logger.info("商品刪除成功，商品 ID: {}", id);
            return ResponseEntity.noContent().build();
        } catch (ProductNotFoundException e) {
            logger.warn("刪除商品失敗: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    public static class ProductAlreadyExistsException extends RuntimeException {
        public ProductAlreadyExistsException(String message) {
            super(message);
        }
    }

    public static class ProductNotFoundException extends RuntimeException {
        public ProductNotFoundException(String message) {
            super(message);
        }
    }
}