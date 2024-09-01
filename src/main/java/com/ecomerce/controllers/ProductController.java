package com.ecomerce.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecomerce.entities.Product;
import com.ecomerce.entities.Sale;
import com.ecomerce.services.ProductService;
import com.ecomerce.services.SaleService;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private SaleService saleService;

    @PostMapping("/add")
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
        try {
            Product novoProduct = productService.addProduct(product);
            return new ResponseEntity<>(novoProduct, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/addsale")
    public ResponseEntity<Sale> adicionarProdutosAVenda(
    	    @RequestBody List<Long> productIds, 
    	    @RequestParam(required = false) Long saleId) {
    	    try {
    	        Sale saleAtualizada = saleService.addProduct(productIds, saleId);
    	        return new ResponseEntity<>(saleAtualizada, HttpStatus.OK);
    	    } catch (Exception e) {
    	        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    	    }
    }
}
