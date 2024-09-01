package com.ecomerce.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecomerce.entities.Product;
import com.ecomerce.entities.User;
import com.ecomerce.services.ProductService;
import com.ecomerce.services.UserService;

@RequestMapping("/admin")
@RestController
public class AdminController {

    @Autowired
    UserService userService;

    @Autowired
    ProductService productService;

    // deletar produto se possível
    @DeleteMapping("delete-product/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        Optional<Product> optionalProduct = productService.getProduct(id);
        if (optionalProduct.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Product product = optionalProduct.get();
        if (!"DISPONÍVEL".equalsIgnoreCase(product.getStatus())) {
            return ResponseEntity.status(400).body("Produto não está disponível para exclusão");
        }

        productService.deleteProduct(id);
        return ResponseEntity.ok("Produto deletado com sucesso");
    }

    // Criar Produtos
    @SuppressWarnings("null")
    @PostMapping("/add-product")
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
        try {
            Product novoProduct = productService.addProduct(product);
            return new ResponseEntity<>(novoProduct, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Criar Usuarios
    @SuppressWarnings("null")
    @PostMapping("/add-user")
    public ResponseEntity<User> criarUser(@RequestBody User user) {
        try {
            User novoUser = userService.addUser(user);
            return new ResponseEntity<>(novoUser, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
