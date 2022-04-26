package com.profeco.consumer.controller;

import com.profeco.consumer.dto.ProductList;
import com.profeco.consumer.entities.Consumer;
import com.profeco.consumer.entities.Product;
import com.profeco.consumer.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<List<Product>> listProducts(@RequestParam(name = "name" , required = false) String name) {
        List<Product> products = name == null || name.isEmpty() ?
                productService.findAll()
                :productService.findByName(name);

        if (products.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(products);
    }
}
