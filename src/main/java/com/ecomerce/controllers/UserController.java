package com.ecomerce.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
@RequestMapping("/user")
public class UserController {

	@Autowired
	ProductService productService;
	
	@Autowired
	SaleService saleService;

	@PostMapping("/confirm")
    public ResponseEntity<Void> confirmSale(@RequestBody List<Long> saleIds) {
        saleService.confirmSale(saleIds);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/cancel")
    public ResponseEntity<Void> cancelSale(@RequestBody List<Long> saleIds) {
        saleService.cancelSale(saleIds);
        return ResponseEntity.ok().build();
    }

	@GetMapping("/products")
	public ResponseEntity<List<Product>> listProducts() {
        List<Product> produtos = productService.listProducts();
        return ResponseEntity.ok(produtos);
    }

	@GetMapping("/sales")
	public ResponseEntity<List<Sale>> listSales() {
        List<Sale> sales = saleService.listSales();
        return ResponseEntity.ok(sales);
    }

	// adiciona produto a compra
	@PostMapping("/add-product-to-sale")
	public ResponseEntity<Sale> addProductToSale(
			@RequestParam(required = false) Long saleId,
			@RequestBody List<Long> productIds) {
		Sale sale = saleService.addProduct(productIds, Optional.ofNullable(saleId));
		return ResponseEntity.ok(sale);
	}

	
}
