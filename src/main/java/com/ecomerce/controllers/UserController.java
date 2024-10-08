package com.ecomerce.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecomerce.entities.Product;
import com.ecomerce.entities.Sale;
import com.ecomerce.security.PasswordChangeRequest;
import com.ecomerce.services.ProductService;
import com.ecomerce.services.SaleService;
import com.ecomerce.services.UserService;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    ProductService productService;

    @Autowired
    SaleService saleService;

    // Confirma Compra
    @PostMapping("/confirm")
    @CacheEvict(value = {"products", "sales"}, allEntries = true)
    public ResponseEntity<String> confirmSale(@RequestBody List<Long> saleIds) {
        try {
            saleService.confirmSale(saleIds);
            return ResponseEntity.ok("Compra confirmada com sucesso! Obrigado.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro: " + e.getMessage());
        }
    }

    // Cancela Compra
    @PostMapping("/cancel")
    @CacheEvict(value = {"products", "sales"}, allEntries = true)
    public ResponseEntity<String> cancelSale(@RequestBody List<Long> saleIds) {
        try {
            saleService.cancelSale(saleIds);
            return ResponseEntity.ok("Compra cancelada com sucesso! Obrigado.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro: " + e.getMessage());
        }
    }

    // Vê a lista de produts disponíveis
    @GetMapping("/products")
    @Cacheable("products")
    public ResponseEntity<List<Product>> listProducts() {

        System.out.println("add cache - products");

        try {
            List<Product> produtos = productService.listProducts();
            return ResponseEntity.ok(produtos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null); // Pode melhorar a mensagem de erro conforme o caso
        }
    }

    // Vê a lista de pedidos do usuário logado
    @GetMapping("/sales")
    @Transactional
    @Cacheable("sales")
    public ResponseEntity<List<Sale>> listSales() {

        System.out.println("add cache - sales");

        try {
            List<Sale> sales = saleService.listSales();
            return ResponseEntity.ok(sales);
        } catch (Exception e) {
            System.out.println("erro aqui");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    // Adiciona produtos a uma venda
    @PostMapping("/add-product-to-sale")
    @CacheEvict(value = {"products", "sales"}, allEntries = true)
    public ResponseEntity<String> addProductToSale(
            @RequestParam(required = false) Long saleId,
            @RequestBody List<Long> productIds) {
        try {
            Sale sale = saleService.addProduct(productIds, Optional.ofNullable(saleId));
            return ResponseEntity.ok("Produtos adicionados à venda com sucesso. Venda ID: " + sale.getId());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro: " + e.getMessage());
        }
    }

    // Trocar Senha
    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody PasswordChangeRequest request) {
        boolean isChanged = userService.changePassword(request.getOldPassword(), request.getNewPassword());
        if (isChanged) {
            return ResponseEntity.ok("Senha alterada com sucesso!");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro: Senha antiga está incorreta. Verifique e tente novamente.");
        }
    }
}
